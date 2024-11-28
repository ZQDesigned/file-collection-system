import type { FC } from "react";
import type { NavigateFunction, Location } from "react-router-dom";
import { Menu, Badge } from "antd";
import { HomeOutlined, PlusCircleOutlined, DownloadOutlined } from "@ant-design/icons";
import { RoutePath } from "@/shared";

export interface NavigationMenuProps {
  navigate: NavigateFunction;
  location: Location;
  pendingTasksCount: number;
}

export const NavigationMenu: FC<NavigationMenuProps> = (props) => {
  const { navigate, location, pendingTasksCount } = props;
  const menuItems = [
    { key: RoutePath.Home, icon: <HomeOutlined />, label: "首页" },
    { key: RoutePath.Create, icon: <PlusCircleOutlined />, label: "创建任务" },
    {
      key: RoutePath.Downloads,
      icon: (
        <Badge count={pendingTasksCount} offset={[10, 0]}>
          <DownloadOutlined />
        </Badge>
      ),
      label: "下载任务",
    },
  ];

  return (
    <Menu
      theme="light"
      defaultSelectedKeys={[location.pathname]}
      mode="inline"
      style={{ paddingTop: 12 }}
      items={menuItems.map((item) => ({
        key: item.key,
        icon: item.icon,
        label: item.label,
        onClick: () => navigate(item.key),
      }))}
    />
  );
};