import { DownloadTaskStatus } from "@/shared/enum"
import type { DownloadSettings, DownloadTask, Submission, UploadTask } from "@/shared/types"

// 模拟延迟
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms))

const enum LS_PATH {
  UPLOAD = 'upload',
  DOWNLOAD = 'download',
}

// 模拟文件类型列表
export const fileTypes = [
  { id: '1', label: 'PDF文件', value: 'application/pdf' },
  { id: '2', label: '图片文件', value: 'image/*' },
  { id: '3', label: 'Word文档', value: 'application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document' },
  { id: '4', label: 'Excel表格', value: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' }
]

// 从 localStorage 获取提交任务数据
const getUploadTasks = (): UploadTask[] => {
  const tasksJson = localStorage.getItem(LS_PATH.UPLOAD)
  return tasksJson ? JSON.parse(tasksJson) : []
}

// 保存提交任务数据到 localStorage
const saveUploadTasks = (tasks: UploadTask[]) => {
  localStorage.setItem(LS_PATH.UPLOAD, JSON.stringify(tasks))
}

// 添加下载任务相关的API
export const mockApi = {
  // 获取支持的文件类型列表
  getFileTypes: async () => {
    await delay(500)
    return fileTypes
  },

  // 创建任务
  createUploadTask: async (taskData: Omit<UploadTask, 'id' | 'createdAt' | 'submissions'>) => {
    await delay(800)
    const tasks = getUploadTasks()
    const newTask: UploadTask = {
      id: Date.now().toString(),
      ...taskData,
      createdAt: new Date().toISOString(),
      submissions: []
    }
    tasks.push(newTask)
    saveUploadTasks(tasks)
    return newTask
  },

  // 获取任务列表
  getUploadTasks: async () => {
    await delay(500)
    return getUploadTasks()
  },

  // 获取任务详情
  getUploadTaskDetail: async (taskId: string) => {
    await delay(500)
    const tasks = getUploadTasks()
    const task = tasks.find(t => t.id === taskId)
    if (!task) throw new Error('Task not found')
    return task
  },

  // 提交文件
  submitFiles: async (taskId: string, formData: any, files: File[], token?: string) => {
    await delay(1000)
    const tasks = getUploadTasks()
    const task = tasks.find(t => t.id === taskId)
    if (!task) throw new Error('Task not found')

    // 如果提供了 token，尝试更新现有提交
    if (token) {
      const submissionIndex = task.submissions.findIndex(s => s.accessToken === token)
      if (submissionIndex !== -1) {
        // 更新现有提交
        const updatedSubmission = {
          ...task.submissions[submissionIndex],
          formData,
          files: files.map(file => ({
            name: file.name,
            size: file.size,
            type: file.type
          })),
          submittedAt: new Date().toISOString()
        }
        task.submissions[submissionIndex] = updatedSubmission
        saveUploadTasks(tasks)
        return updatedSubmission
      }
    }

    // 创建新提交
    const accessToken = token || Math.random().toString(36).substring(2, 15)
    const submission: Submission = {
      id: Date.now().toString(),
      taskId,
      formData,
      files: files.map(file => ({
        name: file.name,
        size: file.size,
        type: file.type
      })),
      submittedAt: new Date().toISOString(),
      accessToken,
      accessUrl: `/submission-view/${taskId}/${accessToken}`
    }

    task.submissions.push(submission)
    saveUploadTasks(tasks)
    return submission
  },

  // 删除任务
  deleteUploadTask: async (taskId: string) => {
    await delay(500)
    const tasks = getUploadTasks()
    const index = tasks.findIndex(t => t.id === taskId)
    if (index === -1) throw new Error('Task not found')
    tasks.splice(index, 1)
    saveUploadTasks(tasks)
  },

  // 更新任务
  updateUploadTask: async (taskId: string, taskData: Partial<Omit<UploadTask, 'id' | 'createdAt' | 'submissions'>>) => {
    await delay(800)
    const tasks = getUploadTasks()
    const task = tasks.find(t => t.id === taskId)
    if (!task) throw new Error('Task not found')

    Object.assign(task, taskData)
    saveUploadTasks(tasks)
    return task
  },

  // 下载单个提交的文件
  downloadSubmission: async (taskId: string, submissionId: string) => {
    await delay(500)
    const tasks = getUploadTasks()
    const task = tasks.find(t => t.id === taskId)
    if (!task) throw new Error('Task not found')

    const submission = task.submissions.find(s => s.id === submissionId)
    if (!submission) throw new Error('Submission not found')

    // 在实际项目中，这里会返回文件的下载链接或二进制数据
    alert('文件下载功能在后端实现')
    return submission.files
  },

  // 下载任务的所有提交文件
  downloadAllSubmissions: async (taskId: string) => {
    await delay(800)
    const tasks = getUploadTasks()
    const task = tasks.find(t => t.id === taskId)
    if (!task) throw new Error('Task not found')

    // 在实际项目中，这里会返回一个压缩包的下载链接或二进制数据
    alert('文件打包下载功能将在后端实现')
    return task.submissions
  },

  // 创建下载任务
  createDownloadTask: async (
    taskId: string,
    type: 'single' | 'all',
    settings: DownloadSettings,
    submissionId?: string
  ) => {
    await delay(500)
    const newTask: DownloadTask = {
      id: Date.now().toString(),
      taskId,
      type,
      submissionId,
      settings,
      status: DownloadTaskStatus.Pending,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    }

    // 模拟任务处理
    setTimeout(async () => {
      try {
        newTask.status = DownloadTaskStatus.Processing
        await delay(3000) // 模拟处理时间

        if (Math.random() > 0.8) { // 20%的概率失败
          throw new Error('模拟下载任务失败')
        }

        newTask.status = DownloadTaskStatus.Completed
        newTask.url = `https://example.com/download/${newTask.id}`
      } catch (error) {
        newTask.status = DownloadTaskStatus.Failed
        newTask.error = error instanceof Error ? error.message : '未知错误'
      } finally {
        newTask.updatedAt = new Date().toISOString()
        saveDownloadTasks(getDownloadTasks().map(t =>
          t.id === newTask.id ? newTask : t
        ))
      }
    }, 1000)

    const tasks = getDownloadTasks()
    tasks.push(newTask)
    saveDownloadTasks(tasks)
    return newTask
  },

  // 获取下载任务列表
  getDownloadTasks: async () => {
    await delay(500)
    return getDownloadTasks().filter(task => {
      const createdAt = new Date(task.createdAt)
      const now = new Date()
      const diffDays = (now.getTime() - createdAt.getTime()) / (1000 * 60 * 60 * 24)
      return diffDays <= 7 // 只返回7天内的任务
    })
  },

  // 删除下载任务
  deleteDownloadTask: async (taskId: string) => {
    await delay(500)
    const tasks = getDownloadTasks()
    const index = tasks.findIndex(t => t.id === taskId)
    if (index === -1) throw new Error('Download task not found')
    tasks.splice(index, 1)
    saveDownloadTasks(tasks)
  },

  // 重试下载任务
  retryDownloadTask: async (taskId: string) => {
    await delay(500)
    const tasks = getDownloadTasks()
    const task = tasks.find(t => t.id === taskId)
    if (!task) throw new Error('Download task not found')

    task.status = DownloadTaskStatus.Pending
    task.error = undefined
    task.url = undefined
    task.updatedAt = new Date().toISOString()

    saveDownloadTasks(tasks)

    // 重新触发处理逻辑
    setTimeout(async () => {
      try {
        task.status = DownloadTaskStatus.Processing
        await delay(3000)

        if (Math.random() > 0.8) {
          throw new Error('模拟下载任务失败')
        }

        task.status = DownloadTaskStatus.Completed
        task.url = `https://example.com/download/${task.id}`
      } catch (error) {
        task.status = DownloadTaskStatus.Failed
        task.error = error instanceof Error ? error.message : '未知错误'
      } finally {
        task.updatedAt = new Date().toISOString()
        saveDownloadTasks(getDownloadTasks().map(t =>
          t.id === task.id ? task : t
        ))
      }
    }, 1000)

    return task
  },

  // 添加通过访问令牌获取提交信息的方法
  getSubmissionByToken: async (taskId: string, accessToken: string) => {
    await delay(500)
    const tasks = getUploadTasks()
    const task = tasks.find(t => t.id === taskId)
    if (!task) throw new Error('Task not found')

    const submission = task.submissions.find(s => s.accessToken === accessToken)
    if (!submission) throw new Error('Submission not found')

    return {
      task,
      submission
    }
  },

  // 添加删除提交的方法
  deleteSubmission: async (taskId: string, submissionId: string) => {
    await delay(500)
    const tasks = getUploadTasks()
    const task = tasks.find(t => t.id === taskId)
    if (!task) throw new Error('Task not found')

    const submissionIndex = task.submissions.findIndex(s => s.id === submissionId)
    if (submissionIndex === -1) throw new Error('Submission not found')

    task.submissions.splice(submissionIndex, 1)
    saveUploadTasks(tasks)
  },

  // 通过 token 删除提交
  deleteSubmissionByToken: async (taskId: string, token: string) => {
    await delay(500)
    const tasks = getUploadTasks()
    const task = tasks.find(t => t.id === taskId)
    if (!task) throw new Error('Task not found')

    const submissionIndex = task.submissions.findIndex(s => s.accessToken === token)
    if (submissionIndex === -1) throw new Error('Submission not found')

    task.submissions.splice(submissionIndex, 1)
    saveUploadTasks(tasks)
  }
}

// 辅助函数
const getDownloadTasks = (): DownloadTask[] => {
  const tasksJson = localStorage.getItem('downloadTasks')
  return tasksJson ? JSON.parse(tasksJson) : []
}

const saveDownloadTasks = (tasks: DownloadTask[]) => {
  localStorage.setItem('downloadTasks', JSON.stringify(tasks))
} 