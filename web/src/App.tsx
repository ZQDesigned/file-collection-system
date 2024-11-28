import { FC } from 'react'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { ThemeProvider, CssBaseline, GlobalStyles } from '@mui/material'
import { createTheme, alpha } from '@mui/material/styles'

import MainLayout from './layout'
import Home from './pages/Home'
import TaskCreate from './pages/TaskCreate'
import TaskSubmit from './pages/TaskSubmit'
import TaskDetail from './pages/TaskDetail'
import TaskEdit from './pages/TaskEdit'
import DownloadTasks from './pages/Download'
import SubmissionDetail from './pages/SubmissionDetail'
import SubmissionView from './pages/SubmissionView'
import { RoutePath } from './shared/enum'

// 创建 MD3 风格主题
const theme = createTheme({
  palette: {
    primary: {
      main: '#006495',
      light: '#5092c3',
      dark: '#003a69',
    },
    secondary: {
      main: '#6750A4',
      light: '#9a82db',
      dark: '#352170',
    },
    background: {
      default: '#f8f9fa',
      paper: '#ffffff',
    },
  },
  shape: {
    borderRadius: 16,
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 100,
          textTransform: 'none',
          fontWeight: 500,
        },
        contained: {
          boxShadow: 'none',
          '&:hover': {
            boxShadow: 'none',
          },
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 20,
          boxShadow: '0 2px 8px ' + alpha('#000', 0.1),
        },
      },
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          borderRadius: 20,
        },
      },
    },
    MuiChip: {
      styleOverrides: {
        root: {
          borderRadius: 8,
        },
      },
    },
  },
})

// 添加全局样式
const globalStyles = {
  '*': {
    margin: 0,
    padding: 0,
    boxSizing: 'border-box',
    // 禁止文本选择
    WebkitUserSelect: 'none',
    MozUserSelect: 'none',
    msUserSelect: 'none',
    userSelect: 'none',
  },
  'html, body': {
    // 禁止缩放
    touchAction: 'manipulation',
    // 设置基础字体大小
    fontSize: '16px',
    // 禁止双击缩放
    WebkitTextSizeAdjust: '100%',
  },
  // 允许特定元素（如输入框）的文本选择
  'input, textarea, [contenteditable="true"]': {
    WebkitUserSelect: 'text',
    MozUserSelect: 'text',
    msUserSelect: 'text',
    userSelect: 'text',
  }
}

const App: FC = () => {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <GlobalStyles styles={globalStyles} />
      <BrowserRouter>
        <Routes>
          <Route path={RoutePath.Home} element={<MainLayout />}>
            <Route index element={<Home />} />
            <Route path={RoutePath.Create} element={<TaskCreate />} />
            <Route path={`${RoutePath.TaskDetailBase}/:taskId`} element={<TaskDetail />} />
            <Route path={`${RoutePath.TaskEditBase}/:taskId`} element={<TaskEdit />} />
            <Route path={`${RoutePath.TaskSubmitBase}/:taskId`} element={<TaskSubmit />} />
            <Route path={RoutePath.Downloads} element={<DownloadTasks />} />
            <Route path={`${RoutePath.SubmissionDetailBase}/:taskId/:submissionId`} element={<SubmissionDetail />} />
            <Route path={`${RoutePath.SubmissionViewBase}/:taskId/:token`} element={<SubmissionView />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </ThemeProvider>
  )
}

export default App
