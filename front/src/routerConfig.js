// 以下文件格式为描述路由的协议格式
// 你可以调整 routerConfig 里的内容
// 变量名 routerConfig 为 iceworks 检测关键字，请不要修改名称

import HeaderAsideLayout from './layouts/HeaderAsideLayout';

import Login from './pages/Login';
import NotFound from './pages/NotFound';
import MyWork from "./pages/MyWork/MyWork";

const routerConfig = [
  {
    path: '/mywork',
    layout: HeaderAsideLayout,
    component: MyWork,
  },
  {
    path: '/form',
    layout: HeaderAsideLayout,
    component: NotFound,
  },
  {
    path: '/charts',
    layout: HeaderAsideLayout,
    component: NotFound,
  },
  {
    path: '/profile',
    layout: HeaderAsideLayout,
    component: NotFound,
  },
  {
    path: '/result',
    layout: HeaderAsideLayout,
    component: NotFound,
  },
  {
    path: '/login',
    layout: Login,
    component: Login,
  },
  {
    path: '*',
    layout: HeaderAsideLayout,
    component: NotFound,
  },
];

export default routerConfig;
