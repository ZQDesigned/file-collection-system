import { DownloadTaskStatus } from "./enum"

export interface FileType {
  id: string
  label: string
  value: string
}

export interface UploadTask {
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

export interface FormField {
  id: string
  label: string
  type: string
  required: string
}

export interface DownloadTask {
  id: string
  taskId: string // 关联的任务ID
  type: 'single' | 'all' // 单个提交还是全部提交
  submissionId?: string // 如果是单个提交，则需要提供submissionId
  settings: DownloadSettings
  status: DownloadTaskStatus
  url?: string // 下载链接
  error?: string // 错误信息
  createdAt: string
  updatedAt: string
}

export interface DownloadSettings {
  separateArchive: boolean // 是否每个人单独打包
  namePattern: string // 命名规则，例如: "{name}-{studentId}"
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