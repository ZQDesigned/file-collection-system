import { FC, useEffect, useState } from 'react'
import {
  Box,
  Paper,
  Typography,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  Tooltip,
  Chip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Alert,
  CircularProgress,
  useTheme,
  useMediaQuery,
  Snackbar
} from '@mui/material'
import {
  Refresh as RefreshIcon,
  Delete as DeleteIcon,
  Download as DownloadIcon,
  ContentCopy as CopyIcon,
} from '@mui/icons-material'
import { mockApi, DownloadTask } from '../services/mockApi'
import { useAppDispatch, useAppSelector } from '../store'
import { setTasks, updateTask, removeTask, setLoading, setError } from '../store/slices/downloadSlice'

const statusColors = {
  pending: 'warning',
  processing: 'info',
  completed: 'success',
  failed: 'error'
} as const

const statusLabels = {
  pending: '等待处理',
  processing: '处理中',
  completed: '已完成',
  failed: '失败'
} as const

const DownloadTasks: FC = () => {
  const [selectedTask, setSelectedTask] = useState<DownloadTask | null>(null)
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false)
  const [errorDialogOpen, setErrorDialogOpen] = useState(false)
  const [snackbarOpen, setSnackbarOpen] = useState(false)
  const [snackbarMessage, setSnackbarMessage] = useState('')
  
  const theme = useTheme()
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'))
  const dispatch = useAppDispatch()
  
  const tasks = useAppSelector(state => state.download.tasks)
  const loading = useAppSelector(state => state.download.loading)
  const error = useAppSelector(state => state.download.error)

  useEffect(() => {
    loadTasks()
    const interval = setInterval(loadTasks, 5000)
    return () => clearInterval(interval)
  }, [])

  const loadTasks = async () => {
    try {
      dispatch(setLoading(true))
      const data = await mockApi.getDownloadTasks()
      dispatch(setTasks(data))
      dispatch(setError(null))
    } catch (err) {
      dispatch(setError('加载任务列表失败'))
    } finally {
      dispatch(setLoading(false))
    }
  }

  const handleDelete = async () => {
    if (!selectedTask) return
    try {
      await mockApi.deleteDownloadTask(selectedTask.id)
      dispatch(removeTask(selectedTask.id))
      showSnackbar('任务已删除')
    } catch (err) {
      showSnackbar('删除任务失败')
    }
    setDeleteDialogOpen(false)
    setSelectedTask(null)
  }

  const handleRetry = async (task: DownloadTask) => {
    try {
      const updatedTask = await mockApi.retryDownloadTask(task.id)
      dispatch(updateTask(updatedTask))
      showSnackbar('已重新提交任务')
    } catch (err) {
      showSnackbar('重试任务失败')
    }
  }

  const copyError = (error: string) => {
    navigator.clipboard.writeText(error)
    showSnackbar('错误信息已复制到剪贴板')
  }

  const showSnackbar = (message: string) => {
    setSnackbarMessage(message)
    setSnackbarOpen(true)
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

  return (
    <Box>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h5">下载任务</Typography>
        <Button
          startIcon={<RefreshIcon />}
          onClick={loadTasks}
        >
          刷新
        </Button>
      </Box>

      <TableContainer component={Paper}>
        <Table size={isMobile ? 'small' : 'medium'}>
          <TableHead>
            <TableRow>
              <TableCell>创建时间</TableCell>
              <TableCell>状态</TableCell>
              <TableCell>类型</TableCell>
              <TableCell>设置</TableCell>
              <TableCell align="right">操作</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {tasks.map((task) => (
              <TableRow key={task.id}>
                <TableCell>
                  {new Date(task.createdAt).toLocaleString()}
                </TableCell>
                <TableCell>
                  <Chip
                    label={statusLabels[task.status]}
                    color={statusColors[task.status]}
                    size="small"
                  />
                </TableCell>
                <TableCell>
                  {task.type === 'all' ? '全部文件' : '单个提交'}
                </TableCell>
                <TableCell>
                  <Typography variant="body2" noWrap>
                    {task.settings.separateArchive ? '分别打包' : '统一打包'}
                  </Typography>
                  <Typography variant="caption" color="text.secondary" noWrap>
                    {task.settings.namePattern}
                  </Typography>
                </TableCell>
                <TableCell align="right">
                  <Box display="flex" justifyContent="flex-end" gap={1}>
                    {task.status === 'completed' && task.url && (
                      <Tooltip title="下载文件">
                        <IconButton
                          color="primary"
                          onClick={() => window.open(task.url, '_blank')}
                        >
                          <DownloadIcon />
                        </IconButton>
                      </Tooltip>
                    )}
                    {task.status === 'failed' && (
                      <>
                        <Tooltip title="重试">
                          <IconButton
                            color="primary"
                            onClick={() => handleRetry(task)}
                          >
                            <RefreshIcon />
                          </IconButton>
                        </Tooltip>
                        <Tooltip title="复制错误信息">
                          <IconButton
                            color="error"
                            onClick={() => task.error && copyError(task.error)}
                          >
                            <CopyIcon />
                          </IconButton>
                        </Tooltip>
                      </>
                    )}
                    <Tooltip title="删除">
                      <IconButton
                        color="error"
                        onClick={() => {
                          setSelectedTask(task)
                          setDeleteDialogOpen(true)
                        }}
                      >
                        <DeleteIcon />
                      </IconButton>
                    </Tooltip>
                  </Box>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {tasks.length === 0 && (
        <Box textAlign="center" mt={4}>
          <Typography color="text.secondary">
            暂无下载任务
          </Typography>
        </Box>
      )}

      <Dialog open={deleteDialogOpen} onClose={() => setDeleteDialogOpen(false)}>
        <DialogTitle>确认删除</DialogTitle>
        <DialogContent>
          <Typography>
            确定要删除这个下载任务吗？
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialogOpen(false)}>取消</Button>
          <Button onClick={handleDelete} color="error">
            删除
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar
        open={snackbarOpen}
        autoHideDuration={2000}
        onClose={() => setSnackbarOpen(false)}
        message={snackbarMessage}
      />
    </Box>
  )
}

export default DownloadTasks 