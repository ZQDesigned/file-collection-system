import { DownloadTask, mockApi } from "@/services/mockApi";
import { useDownloadStore } from "@/store";
import { useCallback, useState } from "react";
import { message } from 'antd'
import { useMount, useUnmount } from 'ahooks'

let timer: number | null = null;

async function poll<T>({
  fetcher,
  terminateConditionFn,
  afterEachPoll,
  duration,
}: {
  fetcher: () => Promise<T>;
  terminateConditionFn: (res: T) => boolean;
  afterEachPoll?: (res: T) => void;
  duration: number;
}) {
  let result = await fetcher();
  afterEachPoll?.(result);

  while (!terminateConditionFn(result)) {
    await new Promise<void>(res => {
      timer = setTimeout(res, duration);
    });
    result = await fetcher();
    afterEachPoll?.(result);
  }

  return result
}

export const useDownloadLogic = () => {
  const [selectedTask, setSelectedTask] = useState<DownloadTask | null>(null);
  const [lastUpdated, setLastUpdated] = useState<string | null>(null);
  const [searchKw, setSearchKw] = useState<string | undefined>(undefined);

  const {
    tasks,
    loading,
    error,
    setLoading,
    setError,
    setTasks,
    removeTask,
    updateTask,
  } = useDownloadStore(state => ({
    tasks: state.states.tasks,
    loading: state.states.loading,
    error: state.states.error,
    setLoading: state.actions.setLoading,
    setError: state.actions.setError,
    setTasks: state.actions.setTasks,
    removeTask: state.actions.removeTask,
    updateTask: state.actions.updateTask,
  }));

  const reload = async (trigger: 'manual' | 'polling' = 'polling') => {
    try {
      if (loading && trigger === 'manual') {
        message.info('任务列表加载中，请稍后再试');
        return;
      }
      // 轮询是不是应该静默？
      setLoading(true);
      const data = await mockApi.getDownloadTasks();
      setLastUpdated(new Date().toLocaleString());
      setTasks(data);
      setError(null);
    } catch (e) {
      setError('加载任务列表失败');
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  useMount(() => {
    poll({
      fetcher: reload,
      terminateConditionFn: () => false,
      afterEachPoll: () => {
        console.log('轮询中...');
      },
      duration: 5000,
    });
  });

  useUnmount(() => {
    if (timer) {
      clearTimeout(timer);
    }
  });

  const handleDelete = useCallback(async () => {
    if (!selectedTask) {
      return;
    }

    try {
      await mockApi.deleteDownloadTask(selectedTask.id);
      removeTask(selectedTask.id);
      message.success('任务已删除');
    } catch (e) {
      message.error('删除任务失败');
      console.error(e);
    }
    setSelectedTask(null);
  }, [selectedTask, removeTask]);

  const handleRetry = useCallback(async (task: DownloadTask) => {
    try {
      const updatedTask = await mockApi.retryDownloadTask(task.id);
      updateTask(updatedTask);
      message.success('已重新提交任务');
    } catch (e) {
      message.error('重试任务失败');
      console.error(e);
    }
  }, [updateTask]);

  const handleCopyError = useCallback((error: string) => {
    navigator.clipboard.writeText(error);
    message.success('错误信息已复制到剪贴板');
  }, []);

  const handleSearch = useCallback((value?: string) => {
    setSearchKw(value);
  }, []);

  return {
    tasks: searchKw ? tasks.filter(task => task.id.includes(searchKw)) : tasks,
    loading,
    error,
    lastUpdated,
    handleSearch,
    setSelectedTask,
    handleDelete,
    handleRetry,
    handleCopyError,
    reload,
  }
}

