export interface Task {
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