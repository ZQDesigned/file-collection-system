import { createSlice, PayloadAction } from '@reduxjs/toolkit'

interface Task {
  id: string
  title: string
  description: string
  deadline: string
  fileTypes: string[]
  maxFiles: number
  formFields: {
    id: string
    label: string
    type: string
    required: boolean
  }[]
}

interface TaskState {
  tasks: Task[]
  currentTask: Task | null
  loading: boolean
  error: string | null
}

const initialState: TaskState = {
  tasks: [],
  currentTask: null,
  loading: false,
  error: null,
}

const taskSlice = createSlice({
  name: 'task',
  initialState,
  reducers: {
    setTasks: (state, action: PayloadAction<Task[]>) => {
      state.tasks = action.payload
    },
    setCurrentTask: (state, action: PayloadAction<Task | null>) => {
      state.currentTask = action.payload
    },
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload
    },
    setError: (state, action: PayloadAction<string | null>) => {
      state.error = action.payload
    },
  },
})

export const { setTasks, setCurrentTask, setLoading, setError } = taskSlice.actions
export default taskSlice.reducer 