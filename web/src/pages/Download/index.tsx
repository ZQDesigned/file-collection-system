import { FC } from 'react';

import { ReloadOutlined } from '@ant-design/icons';
import { LoadingOutlined } from '@ant-design/icons';
import { Button, Input, Spin, Table } from 'antd';

import { useDownloadLogic } from './logic/useDownloadLogic';
import { useTableColumn } from './logic/useTableColumn';
import { Header, HeaderLeft, HeaderRight, LastUpdated } from './style';

const DownloadTasks: FC = () => {
  const {
    tasks,
    lastUpdated,
    loading,
    setSelectedTask,
    reload,
    handleDelete,
    handleRetry,
    handleCopyError,
    handleSearch,
  } = useDownloadLogic();

  const columns = useTableColumn({
    handleRetry,
    handleCopyError,
    handleDelete,
    setSelectedTask,
  });

  return (
    <>
      <Header>
        <HeaderLeft>
          <LastUpdated>上次更新时间: {lastUpdated}</LastUpdated>
          <Spin
            indicator={<LoadingOutlined spin />}
            spinning={loading}
            size="small"
          />
        </HeaderLeft>
        <HeaderRight>
          <Input.Search
            placeholder="按照task id搜索"
            style={{ marginRight: '8px' }}
            onSearch={handleSearch}
          />
          <Button
            icon={<ReloadOutlined />}
            onClick={() => reload('manual')}
            disabled={loading}
          >
            刷新
          </Button>
        </HeaderRight>
      </Header>
      <Table dataSource={tasks} rowKey="id" columns={columns} />
    </>
  );
};

export default DownloadTasks;
