import styled from 'styled-components';
import { Layout, Card } from 'antd';

const { Sider, Content } = Layout;

export const StyledLayout = styled(Layout)`
  height: 100vh;
  background-color: #f0f2f5;
  position: relative;
`;

export const StyledSider = styled(Sider)`
  background-color: #fff;
  position: relative;
`;

export const StyledContent = styled(Content)`
  padding: 24px;
`;

export const StyledCard = styled(Card)`
  min-height: 100%;
  background-color: #fff;
  border-radius: 8px;
`; 