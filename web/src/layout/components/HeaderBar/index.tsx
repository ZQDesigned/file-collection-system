import { useMemo, type FC } from "react";
import type { NavigateFunction } from "react-router-dom";
import type { MenuProps } from "antd";
import {
  UserOutlined,
  MenuUnfoldOutlined,
  MenuFoldOutlined,
} from "@ant-design/icons";
import { Avatar, Dropdown } from "antd";
import { StyledHeader, StyledButton, TitleSpan, RightDiv } from "./style";

export interface HeaderBarProps {
  isSubmitPage: boolean;
  toggleCollapsed: () => void;
  collapsed: boolean;
  navigate: NavigateFunction;
  isMobile: boolean;
}

const useUserDropdownItems: () => MenuProps["items"] = () =>
  useMemo(() => [{
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
    }],
    [],
  );

export const HeaderBar: FC<HeaderBarProps> = (props) => {
  const { isSubmitPage, toggleCollapsed, collapsed, navigate, isMobile } = props;
  const userDropdownItems = useUserDropdownItems();
  return (
    <StyledHeader>
      <div>
        {!isSubmitPage && isMobile && (
          <StyledButton
            type="text"
            icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
            onClick={toggleCollapsed}
          />
        )}
        <TitleSpan onClick={() => !isSubmitPage && navigate("/")}>
          文件收集系统
        </TitleSpan>
      </div>
      <RightDiv>
        <Dropdown
          menu={{ items: userDropdownItems, style: { width: 120 } }}
          trigger={["click"]}
          placement="bottomRight"
        >
          <Avatar style={{ cursor: "pointer" }} icon={<UserOutlined />} />
        </Dropdown>
      </RightDiv>
    </StyledHeader>
  );
};