import { FC, useEffect, useState } from 'react'
import {
  Box,
  Paper,
  Typography,
  Button,
  Grid,
  Chip,
  CircularProgress,
  Alert,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  useTheme,
  useMediaQuery,
  Tooltip,
  Snackbar,
  Switch,
  TextField,
  FormControlLabel
} from '@mui/material'
import {
  Download as DownloadIcon,
  Delete as DeleteIcon,
  Edit as EditIcon,
  ContentCopy as CopyIcon
} from '@mui/icons-material'
import { useParams, useNavigate } from 'react-router-dom'
import { mockApi, Task, Submission, fileTypes } from '../services/mockApi'

const TaskDetail: FC = () => {
  const { taskId } = useParams<{ taskId: string }>()
  const navigate = useNavigate()
  const theme = useTheme()
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'))
  const [task, setTask] = useState<Task | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false)
  const [snackbarOpen, setSnackbarOpen] = useState(false)
  const [downloadSettingsOpen, setDownloadSettingsOpen] = useState(false)
  const [downloadSettings, setDownloadSettings] = useState({
    separateArchive: false,
    namePattern: '{name}'
  })
  const [selectedSubmissionId, setSelectedSubmissionId] = useState<string | null>(null)
  const [selectedSubmissionForDelete, setSelectedSubmissionForDelete] = useState<string | null>(null)
  const [deleteSubmissionDialogOpen, setDeleteSubmissionDialogOpen] = useState(false)
  const [snackbarMessage, setSnackbarMessage] = useState('')

  useEffect(() => {
    loadTask()
  }, [taskId])

  const loadTask = async () => {
    try {
      setLoading(true)
      const data = await mockApi.getTask(taskId!)
      setTask(data)
    } catch (err) {
      setError('加载任务失败')
    } finally {
      setLoading(false)
    }
  }

  const handleDelete = async () => {
    try {
      await mockApi.deleteTask(taskId!)
      navigate('/')
    } catch (err) {
      setError('删除任务失败')
    }
    setDeleteDialogOpen(false)
  }

  const handleDownloadAll = async () => {
    try {
      setDownloadSettingsOpen(true)
    } catch (err) {
      setError('下载失败')
    }
  }

  const handleStartDownload = async () => {
    try {
      if (selectedSubmissionId) {
        await mockApi.createDownloadTask(taskId!, 'single', downloadSettings, selectedSubmissionId)
      } else {
        await mockApi.createDownloadTask(taskId!, 'all', downloadSettings)
      }
      setDownloadSettingsOpen(false)
      setSelectedSubmissionId(null)
      navigate('/downloads')
    } catch (err) {
      setError('创建下载任务失败')
    }
  }

  const handleSingleDownload = async (submissionId: string) => {
    setSelectedSubmissionId(submissionId)
    setDownloadSettingsOpen(true)
  }

  const copySubmitLink = () => {
    const link = `${window.location.origin}/submit/${taskId}`
    navigator.clipboard.writeText(link)
    setSnackbarMessage('提交链接已复制到剪贴板')
    setSnackbarOpen(true)
  }

  const handleDeleteSubmission = async () => {
    if (!selectedSubmissionForDelete) return
    try {
      await mockApi.deleteSubmission(taskId!, selectedSubmissionForDelete)
      await loadTask()
      setSnackbarOpen(true)
      setSnackbarMessage('提交记录已删除')
    } catch (err) {
      setSnackbarOpen(true)
      setSnackbarMessage('删除提交记录失败')
      setError('删除提交记录失败')
    } finally {
      setDeleteSubmissionDialogOpen(false)
      setSelectedSubmissionForDelete(null)
    }
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

  return (
    <Box>
      <Box 
        display="flex" 
        justifyContent="space-between" 
        alignItems="flex-start"
        mb={3}
        gap={2}
      >
        <Typography 
          variant="h5" 
          sx={{ 
            wordBreak: 'break-word',
            flex: 1,
            minWidth: 0,
          }}
        >
          {task?.title}
        </Typography>
        <Box 
          display="flex" 
          gap={1}
          flexShrink={0}
        >
          <Tooltip title="编辑任务">
            <IconButton
              color="primary"
              onClick={() => navigate(`/edit/${taskId}`)}
              sx={{ 
                border: '1px solid',
                borderColor: 'primary.main',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                ...(!isMobile && {
                  borderRadius: 100,
                  px: 2,
                  '& .MuiButton-startIcon': {
                    mr: 0.5
                  }
                })
              }}
            >
              <EditIcon fontSize="small" />
              {!isMobile && <Box component="span" sx={{ ml: 1, typography: 'body2' }}>编辑任务</Box>}
            </IconButton>
          </Tooltip>
          <Tooltip title="复制提交链接">
            <IconButton
              color="primary"
              onClick={copySubmitLink}
              sx={{ 
                border: '1px solid',
                borderColor: 'primary.main',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                ...(!isMobile && {
                  borderRadius: 100,
                  px: 2,
                  '& .MuiButton-startIcon': {
                    mr: 0.5
                  }
                })
              }}
            >
              <CopyIcon fontSize="small" />
              {!isMobile && <Box component="span" sx={{ ml: 1, typography: 'body2' }}>复制提交链接</Box>}
            </IconButton>
          </Tooltip>
          <Tooltip title="删除任务">
            <IconButton
              color="error"
              onClick={() => setDeleteDialogOpen(true)}
              sx={{ 
                border: '1px solid',
                borderColor: 'error.main',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                ...(!isMobile && {
                  borderRadius: 100,
                  px: 2,
                  '& .MuiButton-startIcon': {
                    mr: 0.5
                  }
                })
              }}
            >
              <DeleteIcon fontSize="small" />
              {!isMobile && <Box component="span" sx={{ ml: 1, typography: 'body2' }}>删除任务</Box>}
            </IconButton>
          </Tooltip>
        </Box>
      </Box>

      <Paper sx={{ p: 3, mb: 3 }}>
        <Grid container spacing={3}>
          <Grid item xs={12}>
            <Typography variant="body1" color="text.secondary">
              {task?.description}
            </Typography>
          </Grid>
          <Grid item xs={12} sm={6}>
            <Typography variant="subtitle2" gutterBottom>
              截止日期
            </Typography>
            <Typography>
              {task && new Date(task.deadline).toLocaleString()}
            </Typography>
          </Grid>
          <Grid item xs={12} sm={6}>
            <Typography variant="subtitle2" gutterBottom>
              最大文件数
            </Typography>
            <Typography>{task?.maxFiles}</Typography>
          </Grid>
          <Grid item xs={12}>
            <Typography variant="subtitle2" gutterBottom>
              允许的文件类型
            </Typography>
            <Box display="flex" gap={1} flexWrap="wrap">
              {task?.fileTypes.map((type) => (
                <Chip
                  key={type}
                  label={fileTypes.find(t => t.value === type)?.label || type}
                  size="small"
                />
              ))}
            </Box>
          </Grid>
        </Grid>
      </Paper>

      <Box mb={3}>
        <Box 
          display="flex" 
          justifyContent="space-between" 
          alignItems="center" 
          mb={2}
        >
          <Typography variant="h6">
            提交记录 ({task?.submissions.length || 0})
          </Typography>
          <Tooltip title="下载全部文件">
            <IconButton
              color="primary"
              onClick={handleDownloadAll}
              disabled={!task?.submissions.length}
              sx={{ 
                border: '1px solid',
                borderColor: 'primary.main',
                ...(!isMobile && {
                  borderRadius: 100,
                  px: 2,
                  '& .MuiButton-startIcon': {
                    mr: 0.5
                  }
                })
              }}
            >
              <DownloadIcon fontSize="small" />
              {!isMobile && <Box component="span" sx={{ ml: 1, typography: 'body2' }}>下载全部文件</Box>}
            </IconButton>
          </Tooltip>
        </Box>

        <TableContainer component={Paper}>
          <Table size={isMobile ? 'small' : 'medium'}>
            <TableHead>
              <TableRow>
                <TableCell>提交时间</TableCell>
                <TableCell>表单数据</TableCell>
                <TableCell>文件</TableCell>
                <TableCell align="right">操作</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {task?.submissions.map((submission) => (
                <TableRow key={submission.id}>
                  <TableCell>
                    {new Date(submission.submittedAt).toLocaleString()}
                  </TableCell>
                  <TableCell>
                    <Box sx={{ maxWidth: isMobile ? 120 : 'none', overflow: 'hidden' }}>
                      {Object.entries(submission.formData).map(([key, value]) => {
                        const field = task.formFields.find(f => f.id === key)
                        return field ? (
                          <Box key={key}>
                            <Typography variant="caption" color="text.secondary" noWrap>
                              {field.label}:
                            </Typography>
                            <Typography variant="body2" noWrap>
                              {value}
                            </Typography>
                          </Box>
                        ) : null
                      })}
                    </Box>
                  </TableCell>
                  <TableCell>
                    <Box sx={{ maxWidth: isMobile ? 120 : 'none', overflow: 'hidden' }}>
                      {submission.files.map((file, index) => (
                        <Typography key={index} variant="body2" noWrap>
                          {file.name} ({(file.size / 1024 / 1024).toFixed(2)} MB)
                        </Typography>
                      ))}
                    </Box>
                  </TableCell>
                  <TableCell align="right">
                    <Box display="flex" justifyContent="flex-end" gap={1}>
                      <Tooltip title="下载文件">
                        <IconButton
                          onClick={() => handleSingleDownload(submission.id)}
                        >
                          <DownloadIcon fontSize="small" />
                        </IconButton>
                      </Tooltip>
                      <Tooltip title="删除提交">
                        <IconButton
                          color="error"
                          onClick={() => {
                            setSelectedSubmissionForDelete(submission.id)
                            setDeleteSubmissionDialogOpen(true)
                          }}
                        >
                          <DeleteIcon fontSize="small" />
                        </IconButton>
                      </Tooltip>
                    </Box>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Box>

      <Dialog open={deleteDialogOpen} onClose={() => setDeleteDialogOpen(false)}>
        <DialogTitle>确认删除</DialogTitle>
        <DialogContent>
          <Typography>
            确定要删除这个任务吗？此操作不可恢复。
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialogOpen(false)}>取消</Button>
          <Button onClick={handleDelete} color="error">
            删除
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog 
        open={downloadSettingsOpen} 
        onClose={() => {
          setDownloadSettingsOpen(false)
          setSelectedSubmissionId(null)
        }}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>
          {selectedSubmissionId ? '下载单个提交' : '下载全部文件'}
        </DialogTitle>
        <DialogContent>
          <Box sx={{ pt: 2 }}>
            {!selectedSubmissionId && (
              <FormControlLabel
                control={
                  <Switch
                    checked={downloadSettings.separateArchive}
                    onChange={(e) => setDownloadSettings({
                      ...downloadSettings,
                      separateArchive: e.target.checked
                    })}
                  />
                }
                label="每个提交单独打包"
              />
            )}
            <Box sx={{ mt: 3 }}>
              <TextField
                label="文件（夹）命名规则"
                fullWidth
                value={downloadSettings.namePattern}
                onChange={(e) => setDownloadSettings({
                  ...downloadSettings,
                  namePattern: e.target.value
                })}
                helperText={
                  <Typography variant="caption">
                    可用变量：<br />
                    {task?.formFields.map(field => `{${field.label}}`).join(', ')}
                    <br />
                    示例：学号{'{学号}'}_姓名{'{姓名}'}
                  </Typography>
                }
              />
            </Box>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => {
            setDownloadSettingsOpen(false)
            setSelectedSubmissionId(null)
          }}>
            取消
          </Button>
          <Button onClick={handleStartDownload} variant="contained">
            开始下载
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog 
        open={deleteSubmissionDialogOpen} 
        onClose={() => {
          setDeleteSubmissionDialogOpen(false)
          setSelectedSubmissionForDelete(null)
        }}
      >
        <DialogTitle>确认删除提交记录</DialogTitle>
        <DialogContent>
          <Typography>
            确定要删除这条提交记录吗？此操作不可恢复。
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => {
            setDeleteSubmissionDialogOpen(false)
            setSelectedSubmissionForDelete(null)
          }}>
            取消
          </Button>
          <Button onClick={handleDeleteSubmission} color="error">
            删除
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar
        open={snackbarOpen}
        autoHideDuration={2000}
        onClose={() => {
          setSnackbarOpen(false)
          setSnackbarMessage('')
        }}
        message={snackbarMessage}
      />
    </Box>
  )
}

export default TaskDetail 