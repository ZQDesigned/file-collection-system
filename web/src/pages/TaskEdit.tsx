import { FC, useEffect, useState } from 'react'
import { useForm, Controller } from 'react-hook-form'
import {
  Box,
  TextField,
  Button,
  Paper,
  Typography,
  Grid,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Chip,
  IconButton,
  OutlinedInput,
  Alert,
  CircularProgress
} from '@mui/material'
import { Add as AddIcon, Delete as DeleteIcon } from '@mui/icons-material'
import { useNavigate, useParams } from 'react-router-dom'
import { mockApi, Task, FileType, FormField } from '../services/mockApi'

interface TaskFormData {
  title: string
  description: string
  deadline: string
  fileTypes: string[]
  maxFiles: number
  formFields: FormField[]
}

const TaskEdit: FC = () => {
  const { taskId } = useParams<{ taskId: string }>()
  const navigate = useNavigate()
  const [availableFileTypes, setAvailableFileTypes] = useState<FileType[]>([])
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [formError, setFormError] = useState<string | null>(null)

  const { control, handleSubmit, watch, setValue, reset } = useForm<TaskFormData>()
  const formFields = watch('formFields') || []

  useEffect(() => {
    const loadData = async () => {
      try {
        setLoading(true)
        const [types, task] = await Promise.all([
          mockApi.getFileTypes(),
          mockApi.getTask(taskId!)
        ])
        setAvailableFileTypes(types)
        reset({
          title: task.title,
          description: task.description,
          deadline: task.deadline,
          fileTypes: task.fileTypes,
          maxFiles: task.maxFiles,
          formFields: task.formFields
        })
      } catch (error) {
        setFormError('加载任务失败')
        console.error(error)
      } finally {
        setLoading(false)
      }
    }
    loadData()
  }, [taskId, reset])

  const onSubmit = async (data: TaskFormData) => {
    try {
      setSaving(true)
      await mockApi.updateTask(taskId!, data)
      navigate(`/task/${taskId}`)
    } catch (error) {
      setFormError(error instanceof Error ? error.message : '保存失败')
    } finally {
      setSaving(false)
    }
  }

  const addFormField = () => {
    const newField: FormField = {
      id: Date.now().toString(),
      label: '',
      type: 'text',
      required: 'false'
    }
    setValue('formFields', [...formFields, newField])
  }

  const removeFormField = (id: string) => {
    setValue('formFields', formFields.filter(field => field.id !== id))
  }

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="200px">
        <CircularProgress />
      </Box>
    )
  }

  return (
    <Paper sx={{ p: 3 }}>
      {formError && (
        <Alert severity="error" sx={{ mb: 2 }} onClose={() => setFormError(null)}>
          {formError}
        </Alert>
      )}
      <Typography variant="h5" gutterBottom>
        编辑任务
      </Typography>
      <Box component="form" onSubmit={handleSubmit(onSubmit)} sx={{ mt: 3 }}>
        <Grid container spacing={3}>
          <Grid item xs={12}>
            <Controller
              name="title"
              control={control}
              rules={{ required: '请输入任务标题' }}
              render={({ field, fieldState: { error } }) => (
                <TextField
                  {...field}
                  label="任务标题"
                  fullWidth
                  error={!!error}
                  helperText={error?.message}
                />
              )}
            />
          </Grid>

          <Grid item xs={12}>
            <Controller
              name="description"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  label="任务描述"
                  multiline
                  rows={4}
                  fullWidth
                />
              )}
            />
          </Grid>

          <Grid item xs={12} md={6}>
            <Controller
              name="deadline"
              control={control}
              rules={{ required: '请选择截止日期' }}
              render={({ field, fieldState: { error } }) => (
                <TextField
                  {...field}
                  type="datetime-local"
                  label="截止日期"
                  fullWidth
                  InputLabelProps={{ shrink: true }}
                  error={!!error}
                  helperText={error?.message}
                />
              )}
            />
          </Grid>

          <Grid item xs={12} md={6}>
            <Controller
              name="maxFiles"
              control={control}
              rules={{ required: '请输入最大文件数', min: 1 }}
              render={({ field, fieldState: { error } }) => (
                <TextField
                  {...field}
                  type="number"
                  label="最大文件数"
                  fullWidth
                  error={!!error}
                  helperText={error?.message}
                  inputProps={{ min: 1 }}
                />
              )}
            />
          </Grid>

          <Grid item xs={12}>
            <Controller
              name="fileTypes"
              control={control}
              rules={{ required: '请选择允许的文件类型' }}
              render={({ field, fieldState: { error } }) => (
                <FormControl fullWidth error={!!error}>
                  <InputLabel>允许的文件类型</InputLabel>
                  <Select
                    {...field}
                    multiple
                    value={Array.isArray(field.value) ? field.value : []}
                    input={<OutlinedInput label="允许的文件类型" />}
                    renderValue={(selected) => (
                      <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                        {(Array.isArray(selected) ? selected : []).map((value) => {
                          const fileType = availableFileTypes.find(type => type.value === value)
                          return (
                            <Chip 
                              key={value} 
                              label={fileType?.label || value} 
                              size="small"
                            />
                          )
                        })}
                      </Box>
                    )}
                  >
                    {availableFileTypes.map((type) => (
                      <MenuItem key={type.id} value={type.value}>
                        {type.label}
                      </MenuItem>
                    ))}
                  </Select>
                  {error && (
                    <Typography color="error" variant="caption" sx={{ mt: 1 }}>
                      {error.message}
                    </Typography>
                  )}
                </FormControl>
              )}
            />
          </Grid>

          <Grid item xs={12}>
            <Typography variant="h6" gutterBottom>
              表单字段
              <IconButton color="primary" onClick={addFormField} sx={{ ml: 1 }}>
                <AddIcon />
              </IconButton>
            </Typography>
            {formFields.map((field, index) => (
              <Box key={field.id} sx={{ mb: 2, p: 2, border: '1px solid #eee', borderRadius: 1 }}>
                <Grid container spacing={2} alignItems="center">
                  <Grid item xs={12} md={5}>
                    <Controller
                      name={`formFields.${index}.label`}
                      control={control}
                      render={({ field }) => (
                        <TextField {...field} label="字段标签" fullWidth />
                      )}
                    />
                  </Grid>
                  <Grid item xs={12} md={5}>
                    <Controller
                      name={`formFields.${index}.type`}
                      control={control}
                      render={({ field }) => (
                        <FormControl fullWidth>
                          <InputLabel>字段类型</InputLabel>
                          <Select {...field} label="字段类型">
                            <MenuItem value="text">文本</MenuItem>
                            <MenuItem value="number">数字</MenuItem>
                            <MenuItem value="email">邮箱</MenuItem>
                            <MenuItem value="tel">电话</MenuItem>
                          </Select>
                        </FormControl>
                      )}
                    />
                  </Grid>
                  <Grid item xs={12} md={1}>
                    <Controller
                      name={`formFields.${index}.required`}
                      control={control}
                      render={({ field }) => (
                        <FormControl fullWidth>
                          <InputLabel>必填</InputLabel>
                          <Select {...field} label="必填">
                            <MenuItem value="true">是</MenuItem>
                            <MenuItem value="false">否</MenuItem>
                          </Select>
                        </FormControl>
                      )}
                    />
                  </Grid>
                  <Grid item xs={12} md={1}>
                    <IconButton
                      color="error"
                      onClick={() => removeFormField(field.id)}
                    >
                      <DeleteIcon />
                    </IconButton>
                  </Grid>
                </Grid>
              </Box>
            ))}
          </Grid>

          <Grid item xs={12}>
            <Box 
              display="flex" 
              gap={2}
              sx={{
                '& > button': {
                  flex: 1,
                  minWidth: 100, // 确保按钮有最小宽度
                  maxWidth: 'calc(50% - 8px)', // 确保两个按钮宽度相等，减去间距的一半
                }
              }}
            >
              <Button
                type="submit"
                variant="contained"
                color="primary"
                size="large"
                disabled={saving}
              >
                {saving ? '保存中...' : '保存修改'}
              </Button>
              <Button
                variant="outlined"
                size="large"
                onClick={() => navigate(`/task/${taskId}`)}
              >
                取消
              </Button>
            </Box>
          </Grid>
        </Grid>
      </Box>
    </Paper>
  )
}

export default TaskEdit 