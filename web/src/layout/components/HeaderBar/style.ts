import styled from "styled-components";
import { Layout, Button } from "antd";

const { Header } = Layout;

export const StyledHeader = styled(Header)`
  padding: 0;
  background-color: #fff;
  color: #000;
  width: 100%;
  z-index: 1;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const StyledButton = styled(Button)`
  margin-left: 16px;
  color: #000;
`;

export const TitleSpan = styled.span`
  margin-left: 16px;
  font-size: 18px;
  color: #000;
  cursor: pointer;
`;

export const RightDiv = styled.div`
  margin-right: 16px;
`; 