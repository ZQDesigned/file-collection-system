import { createStore } from 'zustand'
import { DownloadTask } from '../services/mockApi'
import { useStoreWithEqualityFn } from 'zustand/traditional'

export interface DownloadStore {
  states: {
    tasks: DownloadTask[]
    loading: boolean
    error: string | null
  },
  actions: {
    setTasks: (tasks: DownloadTask[]) => void
    addTask: (task: DownloadTask) => void
    updateTask: (task: DownloadTask) => void
    removeTask: (taskId: string) => void
    setLoading: (loading: boolean) => void
    setError: (error: string | null) => void
  }
}

export const downloadStore = createStore<DownloadStore>((set, get) => ({
  states: {
    tasks: [],
    loading: false,
    error: null,
  },
  actions: {
    setTasks: (tasks) => set({ states: { ...get().states, tasks } }),
    addTask: (task) => set((state) => ({ states: { ...state.states, tasks: [...state.states.tasks, task] } })),
    updateTask: (task) => set((state) => ({
      states: { ...state.states, tasks: state.states.tasks.map(t => t.id === task.id ? task : t) }
    })),
    removeTask: (taskId) => set((state) => ({
      states: { ...state.states, tasks: state.states.tasks.filter(task => task.id !== taskId) }
    })),
    setLoading: (loading) => set({ states: { ...get().states, loading } }),
    setError: (error) => set({ states: { ...get().states, error } }),
  },
}))



export const useDownloadStore = <U>(selector: (state: DownloadStore) => U) => useStoreWithEqualityFn(downloadStore, selector)
