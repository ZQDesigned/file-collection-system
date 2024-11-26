import { FC, useEffect, useState } from 'react'
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  Grid,
  Chip,
  CircularProgress,
  Alert,
  Snackbar
} from '@mui/material'
import { useNavigate } from 'react-router-dom'
import { mockApi, fileTypes } from '../services/mockApi'
import { Add as AddIcon, Link as LinkIcon } from '@mui/icons-material'

interface Task {
  id: string
  title: string
  description: string
  deadline: string
  fileTypes: string[]
  maxFiles: number
  createdAt: string
  submissions: any[]
}

interface FileType {
  id: string
  label: string
  value: string
}

const Home: FC = () => {
  const [tasks, setTasks] = useState<Task[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const navigate = useNavigate()
  const [snackbarOpen, setSnackbarOpen] = useState(false)

  useEffect(() => {
    loadTasks()
  }, [])

  const loadTasks = async () => {
    try {
      setLoading(true)
      const data = await mockApi.getTasks()
      setTasks(data)
      setError(null)
    } catch (err) {
      setError('加载任务列表失败')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const copySubmitLink = (taskId: string) => {
    const link = `${window.location.origin}/submit/${taskId}`
    navigator.clipboard.writeText(link)
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
        <Typography variant="h5">任务列表</Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => navigate('/create')}
        >
          创建任务
        </Button>
      </Box>

      <Grid container spacing={3}>
        {tasks.map((task) => (
          <Grid item xs={12} md={6} lg={4} key={task.id}>
            <Card 
              sx={{ 
                cursor: 'pointer',
                transition: 'transform 0.2s, box-shadow 0.2s',
                '&:hover': {
                  transform: 'translateY(-4px)',
                  boxShadow: (theme) => theme.shadows[4],
                }
              }}
              onClick={() => navigate(`/task/${task.id}`)}
            >
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  {task.title}
                </Typography>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  {task.description}
                </Typography>
                <Box mt={2} mb={2}>
                  <Typography variant="body2" color="text.secondary">
                    截止日期：{new Date(task.deadline).toLocaleString()}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    已提交：{task.submissions.length}
                  </Typography>
                </Box>
                <Box display="flex" gap={1} mb={2}>
                  {task.fileTypes.map((type: string) => (
                    <Chip
                      key={type}
                      label={fileTypes.find((t: FileType) => t.value === type)?.label || type}
                      size="small"
                    />
                  ))}
                </Box>
                <Box mt={2}>
                  <Button
                    startIcon={<LinkIcon />}
                    variant="outlined"
                    fullWidth
                    onClick={(e) => {
                      e.stopPropagation() // 防止触发卡片点击事件
                      copySubmitLink(task.id)
                    }}
                  >
                    复制提交链接
                  </Button>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {tasks.length === 0 && (
        <Box textAlign="center" mt={4}>
          <Typography color="text.secondary">
            暂无任务，点击右上角创建新任务
          </Typography>
        </Box>
      )}

      <Snackbar
        open={snackbarOpen}
        autoHideDuration={2000}
        onClose={() => setSnackbarOpen(false)}
        message="提交链接已复制到剪贴板"
      />
    </Box>
  )
}

export default Home 