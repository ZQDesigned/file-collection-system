import { DownloadTask } from '@/shared/types'
import { createStore } from 'zustand'
import { useStoreWithEqualityFn } from 'zustand/traditional'

export interface DownloadTaskStore {
  states: {
    tasks: DownloadTask[]
  },
  actions: {
    setTasks: (tasks: DownloadTask[]) => void
    addTask: (task: DownloadTask) => void
    updateTask: (task: DownloadTask) => void
    removeTask: (taskId: string) => void
  }
}

export const downloadTaskStore = createStore<DownloadTaskStore>((set, get) => ({
  states: {
    tasks: [],
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
  },
}))



export const useDownloadStore = <U>(selector: (state: DownloadTaskStore) => U) => useStoreWithEqualityFn(downloadTaskStore, selector)
