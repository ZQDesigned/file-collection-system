import { FC, ReactNode, useState } from 'react'
import { 
  AppBar, 
  Toolbar, 
  Typography, 
  Container, 
  Box,
  Button,
  IconButton,
  Drawer,
  List,
  ListItem,
  ListItemText,
  ListItemIcon,
  useTheme,
  useMediaQuery,
  ListItemButton,
  Badge
} from '@mui/material'
import { 
  Menu as MenuIcon,
  Home as HomeIcon,
  AddCircle as AddCircleIcon,
  Download as DownloadIcon
} from '@mui/icons-material'
import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import { useAppSelector } from '../store'
import { selectPendingTasksCount } from '../store/slices/downloadSlice'

const MainLayout: FC<{ children?: ReactNode }> = () => {
  const [mobileOpen, setMobileOpen] = useState(false)
  const theme = useTheme()
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'))
  const navigate = useNavigate()
  const location = useLocation()
  const pendingTasksCount = useAppSelector(selectPendingTasksCount)

  // 判断是否是提交页面
  const isSubmitPage = location.pathname.startsWith('/submit/')

  const menuItems = [
    { text: '首页', path: '/', icon: <HomeIcon /> },
    { text: '创建任务', path: '/create', icon: <AddCircleIcon /> },
    { 
      text: '下载任务', 
      path: '/downloads', 
      icon: <DownloadIcon />,
      badge: pendingTasksCount
    },
  ]

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen)
  }

  const handleNavigation = (path: string) => {
    navigate(path)
    if (mobileOpen) {
      setMobileOpen(false)
    }
  }

  const drawer = !isSubmitPage && (
    <List>
      {menuItems.map((item) => (
        <ListItem key={item.path} disablePadding>
          <ListItemButton
            onClick={() => handleNavigation(item.path)}
            selected={location.pathname === item.path}
            sx={{
              '&.Mui-selected': {
                backgroundColor: 'primary.light',
                '&:hover': {
                  backgroundColor: 'primary.light',
                }
              }
            }}
          >
            <ListItemIcon sx={{ color: location.pathname === item.path ? 'primary.main' : 'inherit' }}>
              {item.badge ? (
                <Badge badgeContent={item.badge} color="error">
                  {item.icon}
                </Badge>
              ) : (
                item.icon
              )}
            </ListItemIcon>
            <ListItemText primary={item.text} />
          </ListItemButton>
        </ListItem>
      ))}
    </List>
  )

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <AppBar position="static">
        <Toolbar>
          {!isSubmitPage && isMobile && (
            <IconButton
              color="inherit"
              edge="start"
              onClick={handleDrawerToggle}
              sx={{ mr: 2 }}
            >
              <MenuIcon />
            </IconButton>
          )}
          <Typography 
            variant="h6" 
            component="div" 
            sx={{ 
              flexGrow: 1, 
              cursor: isSubmitPage ? 'default' : 'pointer'
            }}
            onClick={() => !isSubmitPage && handleNavigation('/')}
          >
            文件收集系统
          </Typography>
          {!isSubmitPage && !isMobile && (
            <Box sx={{ display: 'flex', gap: 2 }}>
              {menuItems.map((item) => (
                <Button
                  key={item.path}
                  color="inherit"
                  startIcon={
                    item.badge ? (
                      <Badge badgeContent={item.badge} color="error">
                        {item.icon}
                      </Badge>
                    ) : (
                      item.icon
                    )
                  }
                  onClick={() => handleNavigation(item.path)}
                  sx={{
                    backgroundColor: location.pathname === item.path ? 'rgba(255, 255, 255, 0.1)' : 'transparent'
                  }}
                >
                  {item.text}
                </Button>
              ))}
            </Box>
          )}
        </Toolbar>
      </AppBar>

      {!isSubmitPage && (
        <Box component="nav">
          <Drawer
            variant="temporary"
            anchor="left"
            open={mobileOpen}
            onClose={handleDrawerToggle}
            ModalProps={{
              keepMounted: true,
            }}
            sx={{
              display: { xs: 'block', sm: 'none' },
              '& .MuiDrawer-paper': { boxSizing: 'border-box', width: 240 },
            }}
          >
            {drawer}
          </Drawer>
        </Box>
      )}

      <Container component="main" sx={{ mt: 4, mb: 4, flex: 1 }}>
        <Outlet />
      </Container>
    </Box>
  )
}

export default MainLayout 