import { UploadTask } from '@/shared/types'
import { createStore } from 'zustand'
import { useStoreWithEqualityFn } from 'zustand/traditional'

interface UploadTaskStore {
  states: {
    uploadTasks: UploadTask[]
    current: UploadTask | null
  },
  actions: {
    setTasks: (tasks: UploadTask[]) => void
    setCurrent: (task: UploadTask | null) => void
  }
}

export const taskStore = createStore<UploadTaskStore>((set, get) => ({
  states: {
    uploadTasks: [],
    current: null,
    loading: false,
    error: null,
  },
  actions: {
    setTasks: (uploadTasks) => set({ states: { ...get().states, uploadTasks } }),
    setCurrent: (task) => set({ states: { ...get().states, current: task } }),
  },
}))

export const useTaskStore = <U>(selector: (state: UploadTaskStore) => U) => useStoreWithEqualityFn(taskStore, selector)

