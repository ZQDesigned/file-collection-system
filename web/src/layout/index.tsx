import { FC, ReactNode, useCallback, useState } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';

import { Grid, Typography } from 'antd';

import { useDownloadStore } from '../store';
import { HeaderBar, NavigationMenu } from './components';
import { StyledCard, StyledContent, StyledLayout, StyledSider } from './style';
import { DownloadTaskStatus, RoutePath } from '@/shared/enum';

const { useBreakpoint } = Grid;

const usePendingTasksCount = () => {
  const downloadingTasks = useDownloadStore(s => s.states.tasks);
  return downloadingTasks.filter(task =>
    [DownloadTaskStatus.Pending, DownloadTaskStatus.Processing].includes(
      task.status,
    ),
  ).length;
};

const usePageTitle = () => {
  const location = useLocation();
  const pathname = location.pathname;
  switch (pathname) {
    case RoutePath.Create:
      return '创建任务';
    case RoutePath.Downloads:
      return '下载任务';
    case RoutePath.SubmissionDetailBase:
      return '提交详情';
    case RoutePath.SubmissionViewBase:
      return '查看提交';
    case RoutePath.TaskDetailBase:
      return '任务详情';
    case RoutePath.TaskEditBase:
      return '编辑任务';
    case RoutePath.TaskSubmitBase:
      return '提交任务';
    default:
      return '首页';
  }
};

const MainLayout: FC<{ children?: ReactNode }> = () => {
  const [collapsed, setCollapsed] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const pendingTasksCount = usePendingTasksCount();
  const screens = useBreakpoint();

  const isSubmitPage = location.pathname.startsWith(RoutePath.TaskSubmitBase);

  const toggleCollapsed = useCallback(() => {
    setCollapsed(prev => !prev);
  }, []);
  const pageTitle = usePageTitle();

  return (
    <StyledLayout>
      <HeaderBar
        isSubmitPage={isSubmitPage}
        toggleCollapsed={toggleCollapsed}
        collapsed={collapsed}
        navigate={navigate}
        isMobile={!screens.md}
      />
      <StyledLayout>
        {!isSubmitPage && (
          <StyledSider
            collapsible={!screens.sm}
            collapsed={collapsed}
            breakpoint="sm"
            onCollapse={toggleCollapsed}
            collapsedWidth={screens.sm ? 0 : 80}
            trigger={null}
          >
            <NavigationMenu
              navigate={navigate}
              location={location}
              pendingTasksCount={pendingTasksCount}
            />
          </StyledSider>
        )}
        <StyledContent>
          <Typography.Title level={5}>{pageTitle}</Typography.Title>
          <StyledCard>
            <Outlet />
          </StyledCard>
        </StyledContent>
      </StyledLayout>
    </StyledLayout>
  );
};

export default MainLayout;
