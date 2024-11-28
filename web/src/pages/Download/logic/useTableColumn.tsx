import { useMemo } from 'react';

import {
  CopyOutlined,
  DeleteOutlined,
  DownloadOutlined,
  ReloadOutlined,
} from '@ant-design/icons';
import {
  Button,
  Modal,
  TableColumnProps,
  Tag,
  Tooltip,
  Typography,
} from 'antd';

import { DownloadTask } from '@/shared/types';

const statusColors = {
  pending: 'orange',
  processing: 'blue',
  completed: 'green',
  failed: 'red',
} as const;

const statusLabels = {
  pending: '等待处理',
  processing: '处理中',
  completed: '已完成',
  failed: '失败',
} as const;

export const useTableColumn: (params: {
  handleRetry: (task: DownloadTask) => void;
  handleCopyError: (error: string) => void;
  handleDelete: (task: DownloadTask) => void;
  setSelectedTask: (task: DownloadTask) => void;
}) => TableColumnProps<DownloadTask>[] = params => {
  const { handleRetry, handleCopyError, handleDelete, setSelectedTask } =
    params;

  return useMemo<TableColumnProps<DownloadTask>[]>(() => {
    return [
      {
        title: '创建时间',
        dataIndex: 'createdAt',
        render: text => new Date(text).toLocaleString(),
      },
      {
        title: '状态',
        dataIndex: 'status',
        render: (status: keyof typeof statusColors) => (
          <Tag color={statusColors[status]}> {statusLabels[status]} </Tag>
        ),
      },
      {
        title: '类型',
        dataIndex: 'type',
        render: type => (type === 'all' ? '全部文件' : '单个提交'),
      },
      {
        title: '设置',
        dataIndex: 'settings',
        render: settings => (
          <>
            <Typography.Text>
              {settings.separateArchive ? '分别打包' : '统一打包'}
            </Typography.Text>
            <br />
            <Typography.Text type="secondary">
              {settings.namePattern}
            </Typography.Text>
          </>
        ),
      },
      {
        title: '操作',
        dataIndex: 'actions',
        render: (_, task: DownloadTask) => (
          <div
            style={{
              display: 'flex',
              justifyContent: 'flex-end',
              gap: '8px',
            }}
          >
            {task.status === 'completed' && task.url && (
              <Tooltip title="下载文件">
                <Button
                  icon={<DownloadOutlined />}
                  onClick={() => window.open(task.url, '_blank')}
                />
              </Tooltip>
            )}
            {task.status === 'failed' && (
              <>
                <Tooltip title="重试">
                  <Button
                    icon={<ReloadOutlined />}
                    onClick={() => handleRetry(task)}
                  />
                </Tooltip>
                <Tooltip title="复制错误信息">
                  <Button
                    icon={<CopyOutlined />}
                    onClick={() => task.error && handleCopyError(task.error)}
                  />
                </Tooltip>
              </>
            )}
            <Tooltip title="删除">
              <Button
                icon={<DeleteOutlined />}
                onClick={() => {
                  setSelectedTask(task);
                  Modal.confirm({
                    title: '确认删除',
                    content: '确定要删除这个下载任务吗？',
                    onOk: handleDelete,
                  });
                }}
              />
            </Tooltip>
          </div>
        ),
      },
    ];
  }, []);
};
