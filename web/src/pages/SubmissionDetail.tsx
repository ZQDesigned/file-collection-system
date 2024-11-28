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
} from '@mui/material'
import { useParams, Link } from 'react-router-dom'
import { mockApi, Task, Submission } from '../services/mockApi'

const SubmissionDetail: FC = () => {
  const { taskId, submissionId } = useParams<{ taskId: string, submissionId: string }>()
  const [task, setTask] = useState<Task | null>(null)
  const [submission, setSubmission] = useState<Submission | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    loadData()
  }, [taskId, submissionId])

  const loadData = async () => {
    try {
      setLoading(true)
      const taskData = await mockApi.getUploadTaskDetail(taskId!)
      setTask(taskData)
      const submissionData = taskData.submissions.find(s => s.id === submissionId)
      if (!submissionData) {
        throw new Error('提交记录不存在')
      }
      setSubmission(submissionData)
    } catch (err) {
      setError(err instanceof Error ? err.message : '加载失败')
    } finally {
      setLoading(false)
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

  if (!task || !submission) {
    return <Alert severity="error">数据不存在</Alert>
  }

  return (
    <Paper sx={{ p: 3 }}>
      <Box mb={3}>
        <Typography variant="h5" gutterBottom>
          提交详情
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
          component={Link}
          to={`/submit/${taskId}`}
        >
          修改提交
        </Button>
        <Button
          variant="outlined"
          component={Link}
          to="/"
        >
          返回首页
        </Button>
      </Box>
    </Paper>
  )
}

export default SubmissionDetail 