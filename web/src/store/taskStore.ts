import { Task } from '@/services/mockApi'
import { createStore } from 'zustand'
import { useStoreWithEqualityFn } from 'zustand/traditional'

interface TaskStore {
  states: {
    tasks: Task[]
    currentTask: Task | null
    loading: boolean
    error: string | null
  },
  actions: {
    setTasks: (tasks: Task[]) => void
    setCurrentTask: (task: Task | null) => void
    setLoading: (loading: boolean) => void
    setError: (error: string | null) => void
  }
}

export const taskStore = createStore<TaskStore>((set, get) => ({
  states: {
    tasks: [],
    currentTask: null,
    loading: false,
    error: null,
  },
  actions: {
    setTasks: (tasks) => set({ states: { ...get().states, tasks } }),
    setCurrentTask: (task) => set({ states: { ...get().states, currentTask: task } }),
    setLoading: (loading) => set({ states: { ...get().states, loading } }),
    setError: (error) => set({ states: { ...get().states, error } }),
  },
}))

export const useTaskStore = <U>(selector: (state: TaskStore) => U) => useStoreWithEqualityFn(taskStore, selector)

