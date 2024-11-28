import { FC, useEffect, useState } from 'react';
import { Controller, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';

import { Add as AddIcon, Delete as DeleteIcon } from '@mui/icons-material';
import {
  Alert,
  Box,
  Button,
  Chip,
  FormControl,
  Grid,
  IconButton,
  InputLabel,
  MenuItem,
  OutlinedInput,
  Paper,
  Select,
  TextField,
  Typography,
} from '@mui/material';

// import { useAppDispatch } from '../store'
import { mockApi } from '../services/mockApi';

interface FileType {
  id: string;
  label: string;
  value: string;
}

interface FormField {
  id: string;
  label: string;
  type: string;
  required: string;
}

interface TaskFormData {
  title: string;
  description: string;
  deadline: string;
  fileTypes: string[];
  maxFiles: number;
  formFields: FormField[];
}

const TaskCreate: FC = () => {
  const [availableFileTypes, setAvailableFileTypes] = useState<FileType[]>([]);
  const [loading, setLoading] = useState(false);
  const [formError, setFormError] = useState<string | null>(null);

  const { control, handleSubmit, watch, setValue, setError } =
    useForm<TaskFormData>({
      defaultValues: {
        title: '',
        description: '',
        deadline: '',
        fileTypes: [],
        maxFiles: 1,
        formFields: [],
      },
    });
  // const dispatch = useAppDispatch()
  const formFields = watch('formFields');
  const navigate = useNavigate();

  useEffect(() => {
    const loadFileTypes = async () => {
      try {
        const types = await mockApi.getFileTypes();
        setAvailableFileTypes(types);
      } catch (error) {
        console.error('Failed to load file types:', error);
        setFormError('加载文件类型失败');
      }
    };
    loadFileTypes();
  }, []);

  const onSubmit = async (data: TaskFormData) => {
    try {
      if (!Array.isArray(data.fileTypes) || data.fileTypes.length === 0) {
        throw new Error('请选择至少一种文件类型');
      }

      if (!Array.isArray(data.formFields)) {
        data.formFields = [];
      }

      if (!data.title?.trim()) {
        throw new Error('请输入任务标题');
      }

      if (!data.deadline) {
        throw new Error('请选择截止日期');
      }

      if (!data.maxFiles || data.maxFiles < 1) {
        throw new Error('请设置有效的最大文件数');
      }

      setLoading(true);
      const result = await mockApi.createTask(data);
      console.log('Task created:', result);
      navigate('/');
    } catch (error) {
      setFormError(error instanceof Error ? error.message : '创建任务失败');
    } finally {
      setLoading(false);
    }
  };

  const addFormField = () => {
    const newField: FormField = {
      id: Date.now().toString(),
      label: '',
      type: 'text',
      required: 'false',
    };
    setValue('formFields', [...formFields, newField]);
  };

  const removeFormField = (id: string) => {
    setValue(
      'formFields',
      formFields.filter(field => field.id !== id),
    );
  };

  return (
    <Paper sx={{ p: 3 }}>
      {formError && (
        <Alert
          severity="error"
          sx={{ mb: 2 }}
          onClose={() => setFormError(null)}
        >
          {formError}
        </Alert>
      )}
      <Typography variant="h5" gutterBottom>
        创建新任务
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
                    renderValue={selected => (
                      <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                        {(Array.isArray(selected) ? selected : []).map(
                          value => {
                            const fileType = availableFileTypes.find(
                              type => type.value === value,
                            );
                            return (
                              <Chip
                                key={value}
                                label={fileType?.label || value}
                                size="small"
                              />
                            );
                          },
                        )}
                      </Box>
                    )}
                  >
                    {availableFileTypes.map(type => (
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
              <Box
                key={field.id}
                sx={{ mb: 2, p: 2, border: '1px solid #eee', borderRadius: 1 }}
              >
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
            <Button
              type="submit"
              variant="contained"
              color="primary"
              size="large"
              fullWidth
              disabled={loading}
            >
              {loading ? '创建中...' : '创建任务'}
            </Button>
          </Grid>
        </Grid>
      </Box>
    </Paper>
  );
};

export default TaskCreate;
