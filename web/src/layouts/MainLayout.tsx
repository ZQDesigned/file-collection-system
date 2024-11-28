import { FC, ReactNode, useState } from "react";
import {
  Layout,
  Menu,
  Badge,
  Button,
  Grid,
  Card,
  Avatar,
  Dropdown,
  MenuProps,
} from "antd";
import {
  MenuUnfoldOutlined,
  MenuFoldOutlined,
  HomeOutlined,
  PlusCircleOutlined,
  DownloadOutlined,
  UserOutlined,
} from "@ant-design/icons";
import {
  Outlet,
  useNavigate,
  useLocation,
  NavigateFunction,
  Location,
} from "react-router-dom";
import { useAppSelector } from "../store";
import { selectPendingTasksCount } from "../store/downloadStore";

const { Header, Sider, Content } = Layout;
const { useBreakpoint } = Grid;

const MainLayout: FC<{ children?: ReactNode }> = () => {
  const [collapsed, setCollapsed] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const pendingTasksCount = useAppSelector(selectPendingTasksCount);
  const screens = useBreakpoint();

  const isSubmitPage = location.pathname.startsWith("/submit/");

  const toggleCollapsed = () => {
    setCollapsed(!collapsed);
  };

  return (
    <Layout
      style={{
        height: "100vh",
        backgroundColor: "#f0f2f5",
        position: "relative",
      }}
    >
      <HeaderBar
        isSubmitPage={isSubmitPage}
        toggleCollapsed={toggleCollapsed}
        collapsed={collapsed}
        navigate={navigate}
        isMobile={!screens.md}
      />
      <Layout>
        {!isSubmitPage && (
          <>
            <Sider
              collapsible={!screens.sm}
              collapsed={collapsed}
              breakpoint="sm"
              onCollapse={toggleCollapsed}
              collapsedWidth={screens.sm ? 0 : 80}
              style={{ backgroundColor: "#fff", position: "relative" }}
              trigger={null}
            >
              <NavigationMenu
                navigate={navigate}
                location={location}
                pendingTasksCount={pendingTasksCount}
              />
            </Sider>
          </>
        )}
        <Content style={{ margin: "24px 16px", padding: 24 }}>
          <Card
            style={{
              minHeight: "100%",
              backgroundColor: "#fff",
              borderRadius: 8,
            }}
          >
            <Outlet />
          </Card>
        </Content>
      </Layout>
    </Layout>
  );
};

const NavigationMenu: FC<{
  navigate: NavigateFunction;
  location: Location;
  pendingTasksCount: number;
}> = ({ navigate, location, pendingTasksCount }) => {
  const menuItems = [
    { key: "/", icon: <HomeOutlined />, label: "首页" },
    { key: "/create", icon: <PlusCircleOutlined />, label: "创建任务" },
    {
      key: "/downloads",
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

const HeaderBar: FC<{
  isSubmitPage: boolean;
  toggleCollapsed: () => void;
  collapsed: boolean;
  navigate: NavigateFunction;
  isMobile: boolean;
}> = ({ isSubmitPage, toggleCollapsed, collapsed, navigate, isMobile }) => {
  const items: MenuProps["items"] = [
    {
      key: "1",
      label: "个人设置",
      icon: <UserOutlined />,
      onClick: () => console.log("个人设置"),
    },
    {
      key: "divider",
      type: "divider",
    },
    {
      key: "2",
      label: "退出登录",
      icon: <UserOutlined />,
      onClick: () => console.log("退出登录"),
    },
  ];

  return (
    <Header
      style={{
        padding: 0,
        backgroundColor: "#fff",
        color: "#000",
        width: "100%",
        zIndex: 1,
        boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
      }}
    >
      <div>
        {!isSubmitPage && isMobile && (
          <Button
            type="text"
            icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
            onClick={toggleCollapsed}
            style={{ marginLeft: 16, color: "#000" }}
          />
        )}
        <span
          style={{
            marginLeft: 16,
            fontSize: 18,
            color: "#000",
          }}
          onClick={() => !isSubmitPage && navigate("/")}
        >
          文件收集系统
        </span>
      </div>
      <div style={{ marginRight: 16 }}>
        <Dropdown menu={{ items, style: { width: 120 } }} trigger={["click"]}>
          <Avatar style={{ cursor: "pointer" }} icon={<UserOutlined />} />
        </Dropdown>
      </div>
    </Header>
  );
};

export default MainLayout;
