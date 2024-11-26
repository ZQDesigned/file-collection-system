import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { DownloadTask } from '../../services/mockApi'

interface DownloadState {
  tasks: DownloadTask[]
  loading: boolean
  error: string | null
}

const initialState: DownloadState = {
  tasks: [],
  loading: false,
  error: null
}

const downloadSlice = createSlice({
  name: 'download',
  initialState,
  reducers: {
    setTasks: (state, action: PayloadAction<DownloadTask[]>) => {
      state.tasks = action.payload
    },
    addTask: (state, action: PayloadAction<DownloadTask>) => {
      state.tasks.push(action.payload)
    },
    updateTask: (state, action: PayloadAction<DownloadTask>) => {
      const index = state.tasks.findIndex(task => task.id === action.payload.id)
      if (index !== -1) {
        state.tasks[index] = action.payload
      }
    },
    removeTask: (state, action: PayloadAction<string>) => {
      state.tasks = state.tasks.filter(task => task.id !== action.payload)
    },
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload
    },
    setError: (state, action: PayloadAction<string | null>) => {
      state.error = action.payload
    }
  }
})

export const { setTasks, addTask, updateTask, removeTask, setLoading, setError } = downloadSlice.actions
export default downloadSlice.reducer

// 选择器
export const selectDownloadTasks = (state: { download: DownloadState }) => state.download.tasks
export const selectPendingTasksCount = (state: { download: DownloadState }) => 
  state.download.tasks.filter(task => 
    task.status === 'pending' || task.status === 'processing'
  ).length 