import { FC, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';



import { PlusOutlined } from '@ant-design/icons';
import { Button, Spin, Table, message } from 'antd';



import { mockApi } from '../../services/mockApi';
import { Header, HeaderLeft } from './style';
import { RoutePath } from '@/shared/enum';
import { UploadTask } from '@/shared/types';


const Home: FC = () => {
  const [tasks, setTasks] = useState<UploadTask[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    loadTasks();
  }, []);

  const loadTasks = async () => {
    try {
      setLoading(true);
      const data = await mockApi.getUploadTasks();
      setTasks(data);
    } catch (err) {
      message.error('加载任务列表失败');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const copySubmitLink = (taskId: string) => {
    const link = `${window.location.origin}/submit/${taskId}`;
    navigator.clipboard.writeText(link);
    message.open({
      type: 'success',
      content: '提交链接已复制到剪贴板',
      duration: 2,
    });
  };

  const columns = [
    {
      title: '任务标题',
      dataIndex: 'title',
      key: 'title',
    },
    {
      title: '描述',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: '截止日期',
      dataIndex: 'deadline',
      key: 'deadline',
      render: (deadline: string) => new Date(deadline).toLocaleString(),
    },
    {
      title: '已提交',
      dataIndex: 'submissions',
      key: 'submissions',
      render: (submissions: UploadTask[]) => submissions.length,
    },
    {
      title: '操作',
      key: 'action',
      render: (_text: string, record: UploadTask) => (
        <>
          <Button
            type="link"
            onClick={e => {
              e.stopPropagation();
              copySubmitLink(record.id);
            }}
          >
            复制提交链接
          </Button>
          <Button
            type="link"
            onClick={e => {
              e.stopPropagation();
              navigate(`/task/${record.id}`);
            }}
          >
            查看详情
          </Button>
        </>
      ),
    },
  ];

  return (
    <>
      <Header>
        <HeaderLeft>
          <Button
            type="primary"
            icon={<PlusOutlined />}
            onClick={() => navigate(RoutePath.Create)}
          >
            创建任务
          </Button>
        </HeaderLeft>
      </Header>
      <Spin spinning={loading} size="large">
        <Table dataSource={tasks} columns={columns} rowKey="id" />
      </Spin>
    </>
  );
};

export default Home;