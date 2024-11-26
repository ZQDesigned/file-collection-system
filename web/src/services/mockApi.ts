// 模拟延迟
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms))

// 模拟文件类型列表
export const fileTypes = [
  { id: '1', label: 'PDF文件', value: 'application/pdf' },
  { id: '2', label: '图片文件', value: 'image/*' },
  { id: '3', label: 'Word文档', value: 'application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document' },
  { id: '4', label: 'Excel表格', value: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' }
]

export interface FileType {
  id: string
  label: string
  value: string
}

export interface FormField {
  id: string
  label: string
  type: string
  required: string
}

export interface Task {
  id: string
  title: string
  description: string
  deadline: string
  fileTypes: string[]
  maxFiles: number
  formFields: FormField[]
  createdAt: string
  submissions: Submission[]
}

export interface Submission {
  id: string
  taskId: string
  formData: Record<string, string>
  files: {
    name: string
    size: number
    type: string
  }[]
  submittedAt: string
  accessToken: string
  accessUrl: string
}

export interface DownloadSettings {
  separateArchive: boolean // 是否每个人单独打包
  namePattern: string // 命名规则，例如: "{name}-{studentId}"
}

export interface DownloadTask {
  id: string
  taskId: string // 关联的任务ID
  type: 'single' | 'all' // 单个提交还是全部提交
  submissionId?: string // 如果是单个提交，则需要提供submissionId
  settings: DownloadSettings
  status: 'pending' | 'processing' | 'completed' | 'failed'
  url?: string // 下载链接
  error?: string // 错误信息
  createdAt: string
  updatedAt: string
}

// 从 localStorage 获取任务数据
const getTasks = (): Task[] => {
  const tasksJson = localStorage.getItem('tasks')
  return tasksJson ? JSON.parse(tasksJson) : []
}

// 保存任务数据到 localStorage
const saveTasks = (tasks: Task[]) => {
  localStorage.setItem('tasks', JSON.stringify(tasks))
}

// 添加下载任务相关的API
export const mockApi = {
  // 获取支持的文件类型列表
  getFileTypes: async () => {
    await delay(500)
    return fileTypes
  },

  // 创建任务
  createTask: async (taskData: Omit<Task, 'id' | 'createdAt' | 'submissions'>) => {
    await delay(800)
    const tasks = getTasks()
    const newTask: Task = {
      id: Date.now().toString(),
      ...taskData,
      createdAt: new Date().toISOString(),
      submissions: []
    }
    tasks.push(newTask)
    saveTasks(tasks)
    return newTask
  },

  // 获取任务列表
  getTasks: async () => {
    await delay(500)
    return getTasks()
  },

  // 获取任务详情
  getTask: async (taskId: string) => {
    await delay(500)
    const tasks = getTasks()
    const task = tasks.find(t => t.id === taskId)
    if (!task) throw new Error('Task not found')
    return task
  },

  // 提交文件
  submitFiles: async (taskId: string, formData: any, files: File[], token?: string) => {
    await delay(1000)
    const tasks = getTasks()
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
        saveTasks(tasks)
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
    saveTasks(tasks)
    return submission
  },

  // 删除任务
  deleteTask: async (taskId: string) => {
    await delay(500)
    const tasks = getTasks()
    const index = tasks.findIndex(t => t.id === taskId)
    if (index === -1) throw new Error('Task not found')
    tasks.splice(index, 1)
    saveTasks(tasks)
  },

  // 更新任务
  updateTask: async (taskId: string, taskData: Partial<Omit<Task, 'id' | 'createdAt' | 'submissions'>>) => {
    await delay(800)
    const tasks = getTasks()
    const task = tasks.find(t => t.id === taskId)
    if (!task) throw new Error('Task not found')
    
    Object.assign(task, taskData)
    saveTasks(tasks)
    return task
  },

  // 下载单个提交的文件
  downloadSubmission: async (taskId: string, submissionId: string) => {
    await delay(500)
    const tasks = getTasks()
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
    const tasks = getTasks()
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
      status: 'pending',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    }
    
    // 模拟任务处理
    setTimeout(async () => {
      try {
        newTask.status = 'processing'
        await delay(3000) // 模拟处理时间
        
        if (Math.random() > 0.8) { // 20%的概率失败
          throw new Error('模拟下载任务失败')
        }
        
        newTask.status = 'completed'
        newTask.url = `https://example.com/download/${newTask.id}`
      } catch (error) {
        newTask.status = 'failed'
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
    
    task.status = 'pending'
    task.error = undefined
    task.url = undefined
    task.updatedAt = new Date().toISOString()
    
    saveDownloadTasks(tasks)
    
    // 重新触发处理逻辑
    setTimeout(async () => {
      try {
        task.status = 'processing'
        await delay(3000)
        
        if (Math.random() > 0.8) {
          throw new Error('模拟下载任务失败')
        }
        
        task.status = 'completed'
        task.url = `https://example.com/download/${task.id}`
      } catch (error) {
        task.status = 'failed'
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
    const tasks = getTasks()
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
    const tasks = getTasks()
    const task = tasks.find(t => t.id === taskId)
    if (!task) throw new Error('Task not found')
    
    const submissionIndex = task.submissions.findIndex(s => s.id === submissionId)
    if (submissionIndex === -1) throw new Error('Submission not found')
    
    task.submissions.splice(submissionIndex, 1)
    saveTasks(tasks)
  },

  // 通过 token 删除提交
  deleteSubmissionByToken: async (taskId: string, token: string) => {
    await delay(500)
    const tasks = getTasks()
    const task = tasks.find(t => t.id === taskId)
    if (!task) throw new Error('Task not found')
    
    const submissionIndex = task.submissions.findIndex(s => s.accessToken === token)
    if (submissionIndex === -1) throw new Error('Submission not found')
    
    task.submissions.splice(submissionIndex, 1)
    saveTasks(tasks)
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