import { FC, useEffect, useState } from 'react'
import {
  Box,
  Paper,
  Typography,
  Button,
  Grid,
  CircularProgress,
  Alert,
  Chip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Snackbar
} from '@mui/material'
import { useParams, useNavigate } from 'react-router-dom'
import { mockApi, Task, Submission } from '../services/mockApi'

const SubmissionView: FC = () => {
  const { taskId, token } = useParams<{ taskId: string, token: string }>()
  const [task, setTask] = useState<Task | null>(null)
  const [submission, setSubmission] = useState<Submission | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false)
  const [snackbarOpen, setSnackbarOpen] = useState(false)
  const [snackbarMessage, setSnackbarMessage] = useState('')
  const navigate = useNavigate()

  useEffect(() => {
    loadData()
  }, [taskId, token])

  const loadData = async () => {
    try {
      setLoading(true)
      const { task, submission } = await mockApi.getSubmissionByToken(taskId!, token!)
      setTask(task)
      setSubmission(submission)
    } catch (err) {
      setError(err instanceof Error ? err.message : '加载失败')
    } finally {
      setLoading(false)
    }
  }

  const handleEdit = () => {
    if (submission && task) {
      localStorage.setItem('editSubmission', JSON.stringify({
        formData: submission.formData,
        files: submission.files,
        taskId: task.id,
        submissionId: submission.id,
        token: token
      }))
      navigate(`/submit/${taskId}?token=${token}`)
    }
  }

  const handleDelete = async () => {
    try {
      if (!submission) return
      await mockApi.deleteSubmissionByToken(taskId!, token!)
      setSnackbarMessage('提交已撤销')
      setSnackbarOpen(true)
      setTimeout(() => {
        navigate(`/submit/${taskId}`)
      }, 1500)
    } catch (err) {
      setError('撤销提交失败')
      setSnackbarMessage('撤销提交失败')
      setSnackbarOpen(true)
    }
    setDeleteDialogOpen(false)
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

  if (!task || !submission) {
    return <Alert severity="error">数据不存在</Alert>
  }

  return (
    <Paper sx={{ p: 3 }}>
      <Box mb={3}>
        <Typography variant="h5" gutterBottom>
          {task.title}
        </Typography>
        <Typography variant="body2" color="text.secondary">
          提交时间：{new Date(submission.submittedAt).toLocaleString()}
        </Typography>
      </Box>

      <Box mb={4}>
        <Typography variant="h6" gutterBottom>
          表单内容
        </Typography>
        <Grid container spacing={2}>
          {Object.entries(submission.formData).map(([key, value]) => {
            const field = task.formFields.find(f => f.id === key)
            return field ? (
              <Grid item xs={12} sm={6} key={key}>
                <Typography variant="subtitle2" color="text.secondary">
                  {field.label}
                </Typography>
                <Typography>{value}</Typography>
              </Grid>
            ) : null
          })}
        </Grid>
      </Box>

      <Box>
        <Typography variant="h6" gutterBottom>
          上传的文件
        </Typography>
        <Grid container spacing={1}>
          {submission.files.map((file, index) => (
            <Grid item key={index}>
              <Chip
                label={`${file.name} (${(file.size / 1024 / 1024).toFixed(2)} MB)`}
                variant="outlined"
              />
            </Grid>
          ))}
        </Grid>
      </Box>

      <Box mt={4} display="flex" gap={2}>
        <Button
          variant="contained"
          onClick={handleEdit}
        >
          修改提交
        </Button>
        <Button
          variant="outlined"
          color="error"
          onClick={() => setDeleteDialogOpen(true)}
        >
          撤销提交
        </Button>
      </Box>

      <Dialog open={deleteDialogOpen} onClose={() => setDeleteDialogOpen(false)}>
        <DialogTitle>确认撤销提交</DialogTitle>
        <DialogContent>
          <Typography>
            确定要撤销这次提交吗？此操作不可恢复。
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialogOpen(false)}>取消</Button>
          <Button onClick={handleDelete} color="error">
            撤销
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar
        open={snackbarOpen}
        autoHideDuration={2000}
        onClose={() => setSnackbarOpen(false)}
        message={snackbarMessage}
      />
    </Paper>
  )
}

export default SubmissionView 