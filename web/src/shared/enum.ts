export const enum RoutePath {
  Home = "/",
  Create = "/create",
  Downloads = "/downloads",
  TaskDetailBase = "/task",
  TaskEditBase = "/edit",
  TaskSubmitBase = "/submit",
  SubmissionDetailBase = "/submission",
  SubmissionViewBase = "/submission-view",
}

export const enum DownloadTaskStatus {
  Pending = 'pending',
  Processing = 'processing',
  Completed = 'completed',
  Failed = 'failed',
}
