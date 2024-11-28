import { FC, useEffect, useState } from 'react'
import { useParams, useNavigate, Link, useLocation } from 'react-router-dom'
import {
  Box,
  Typography,
  Paper,
  TextField,
  Button,
  Grid,
  CircularProgress,
  Alert,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Snackbar
} from '@mui/material'
import { useDropzone } from 'react-dropzone'
import { mockApi, Submission } from '../services/mockApi'

interface Task {
  id: string
  title: string
  description: string
  deadline: string
  fileTypes: string[]
  maxFiles: number
  formFields: FormField[]
  submissions: Submission[]
}

interface FormField {
  id: string
  label: string
  type: string
  required: string
}

const TaskSubmit: FC = () => {
  const { taskId } = useParams<{ taskId: string }>()
  const navigate = useNavigate()
  const [task, setTask] = useState<Task | null>(null)
  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [formData, setFormData] = useState<Record<string, string>>({})
  const [files, setFiles] = useState<File[]>([])
  const [submitted, setSubmitted] = useState(false)
  const [submissionId, setSubmissionId] = useState<string | null>(null)
  const [snackbarOpen, setSnackbarOpen] = useState(false)
  const [snackbarMessage, setSnackbarMessage] = useState('')
  const [submissionResult, setSubmissionResult] = useState<Submission | null>(null)
  const location = useLocation()
  const token = new URLSearchParams(location.search).get('token')

  const { getRootProps, getInputProps } = useDropzone({
    accept: task?.fileTypes.reduce((acc, type) => ({ ...acc, [type]: [] }), {}),
    maxFiles: task?.maxFiles || 1,
    onDrop: (acceptedFiles) => {
      if (acceptedFiles.length + files.length > (task?.maxFiles || 1)) {
        alert(`最多只能上传 ${task?.maxFiles} 个文件`)
        return
      }
      setFiles([...files, ...acceptedFiles])
    }
  })

  useEffect(() => {
    if (!taskId) return
    
    const loadData = async () => {
      try {
        setLoading(true)
        const data = await mockApi.getUploadTaskDetail(taskId)
        setTask(data)
        
        // 如果有 token，尝试加载已有的提交
        if (token) {
          try {
            const { submission } = await mockApi.getSubmissionByToken(taskId, token)
            setFormData(submission.formData)
            // 注意：由于浏览器安全限制，我们不能直接设置文件，但可以显示文件信息
            setFiles([])
          } catch (err) {
            console.error('Failed to load submission:', err)
          }
        }
        // 如果没有 token，检查是否有编辑数据
        else {
          const editDataStr = localStorage.getItem('editSubmission')
          if (editDataStr) {
            try {
              const editData = JSON.parse(editDataStr)
              if (editData.taskId === taskId) {
                setFormData(editData.formData)
                setFiles([])
              }
            } catch (err) {
              console.error('Failed to parse edit data:', err)
            }
            localStorage.removeItem('editSubmission')
          }
        }
      } catch (err) {
        setError('加载任务失败')
        console.error(err)
      } finally {
        setLoading(false)
      }
    }

    loadData()
  }, [taskId, token])

  const handleSubmit = async () => {
    if (!task || !taskId) return
    
    // 验证表单
    const missingFields = task.formFields
      .filter(field => field.required === 'true' && !formData[field.id])
      .map(field => field.label)
    
    if (missingFields.length > 0) {
      setError(`请填写必填字段: ${missingFields.join(', ')}`)
      return
    }

    if (files.length === 0) {
      setError('请上传文件')
      return
    }

    try {
      setSubmitting(true)
      const submission = await mockApi.submitFiles(taskId, formData, files, token) // 传递 token
      setSubmissionId(submission.id)
      setSubmitted(true)
      setSubmissionResult(submission)
    } catch (err) {
      setError('提交失败')
      console.error(err)
    } finally {
      setSubmitting(false)
    }
  }

  if (submitted && submissionId && submissionResult) {
    return (
      <Paper sx={{ p: 3 }}>
        <Alert 
          severity="success" 
          sx={{ mb: 3 }}
        >
          提交成功！
        </Alert>
        <Box display="flex" flexDirection="column" gap={2} alignItems="center">
          <Typography variant="h6" gutterBottom>
            您可以通过以下链接查看或修改提交内容：
          </Typography>
          <TextField
            fullWidth
            value={`${window.location.origin}${submissionResult.accessUrl}`}
            InputProps={{
              readOnly: true,
              endAdornment: (
                <Button
                  onClick={() => {
                    navigator.clipboard.writeText(`${window.location.origin}${submissionResult.accessUrl}`)
                    setSnackbarMessage('链接已复制到剪贴板')
                    setSnackbarOpen(true)
                  }}
                  sx={{ 
                    whiteSpace: 'nowrap',  // 防止文字换行
                    minWidth: 'auto',      // 减小按钮最小宽度
                    ml: 1                  // 添加左边距
                  }}
                >
                  复制链接
                </Button>
              )
            }}
          />
          <Typography variant="body2" color="text.secondary">
            请保存此链接以便后续查看或修改提交内容
          </Typography>
          <Button
            variant="outlined"
            onClick={() => {
              setSubmitted(false)
              setSubmissionId(null)
              setSubmissionResult(null)
              setFiles([])
              setFormData({})
            }}
          >
            继续提交新内容
          </Button>
        </Box>
        <Snackbar
          open={snackbarOpen}
          autoHideDuration={2000}
          onClose={() => setSnackbarOpen(false)}
          message={snackbarMessage}
        />
      </Paper>
    )
  }

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="200px">
        <CircularProgress />
      </Box>
    )
  }

  if (error) {
    return <Alert severity="error">{error}</Alert>
  }

  if (!task) {
    return <Alert severity="error">任务不存在</Alert>
  }

  const isDeadlinePassed = new Date(task.deadline) < new Date()

  return (
    <Paper sx={{ p: 3 }}>
      <Typography variant="h5" gutterBottom>
        {task.title}
      </Typography>
      <Typography variant="body1" color="text.secondary" paragraph>
        {task.description}
      </Typography>

      {isDeadlinePassed ? (
        <Alert severity="error" sx={{ mb: 3 }}>
          该任务已过截止日期
        </Alert>
      ) : (
        <>
          <Grid container spacing={3}>
            {task.formFields.map((field) => (
              <Grid item xs={12} key={field.id}>
                {field.type === 'text' && (
                  <TextField
                    label={field.label}
                    required={field.required === 'true'}
                    fullWidth
                    value={formData[field.id] || ''}
                    onChange={(e) => setFormData({ ...formData, [field.id]: e.target.value })}
                  />
                )}
                {field.type === 'number' && (
                  <TextField
                    type="number"
                    label={field.label}
                    required={field.required === 'true'}
                    fullWidth
                    value={formData[field.id] || ''}
                    onChange={(e) => setFormData({ ...formData, [field.id]: e.target.value })}
                  />
                )}
                {field.type === 'email' && (
                  <TextField
                    type="email"
                    label={field.label}
                    required={field.required === 'true'}
                    fullWidth
                    value={formData[field.id] || ''}
                    onChange={(e) => setFormData({ ...formData, [field.id]: e.target.value })}
                  />
                )}
                {field.type === 'tel' && (
                  <TextField
                    type="tel"
                    label={field.label}
                    required={field.required === 'true'}
                    fullWidth
                    value={formData[field.id] || ''}
                    onChange={(e) => setFormData({ ...formData, [field.id]: e.target.value })}
                  />
                )}
              </Grid>
            ))}

            <Grid item xs={12}>
              <Box
                {...getRootProps()}
                sx={{
                  border: '2px dashed #ccc',
                  borderRadius: 1,
                  p: 3,
                  textAlign: 'center',
                  cursor: 'pointer',
                  '&:hover': {
                    borderColor: 'primary.main'
                  }
                }}
              >
                <input {...getInputProps()} />
                <Typography>
                  拖放文件到此处，或点击选择文件
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  最多上传 {task.maxFiles} 个文件
                </Typography>
              </Box>
            </Grid>

            {files.length > 0 && (
              <Grid item xs={12}>
                <Typography variant="subtitle2" gutterBottom>
                  已选择的文件：
                </Typography>
                {files.map((file, index) => (
                  <Box key={index} display="flex" alignItems="center" mb={1}>
                    <Typography variant="body2" sx={{ flex: 1 }}>
                      {file.name} ({(file.size / 1024 / 1024).toFixed(2)} MB)
                    </Typography>
                    <Button
                      size="small"
                      color="error"
                      onClick={() => setFiles(files.filter((_, i) => i !== index))}
                    >
                      删除
                    </Button>
                  </Box>
                ))}
              </Grid>
            )}

            <Grid item xs={12}>
              <Button
                variant="contained"
                fullWidth
                size="large"
                onClick={handleSubmit}
                disabled={submitting || files.length === 0}
              >
                {submitting ? '提交中...' : '提交'}
              </Button>
            </Grid>
          </Grid>
        </>
      )}

      {/* 添加文件列表的提示 */}
      {token && task?.submissions.find(s => s.accessToken === token) && (
        <Alert severity="info" sx={{ mb: 3 }}>
          由于浏览器安全限制，您需要重新上传文件。其他表单数据已自动填充。
        </Alert>
      )}
    </Paper>
  )
}

export default TaskSubmit 