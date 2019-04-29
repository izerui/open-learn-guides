// 菜单配置
// headerMenuConfig：头部导航配置
// asideMenuConfig：侧边导航配置

const headerMenuConfig = [];

const asideMenuConfig = [
  {
    path: '/dashboard',
    name: 'Dashboard',
    icon: 'el-icon-menu',
  },
  {
    path: '/mywork',
    name: '作业列表',
    icon: 'el-icon-date',
  },
  {
    path: '/form',
    name: '表单页',
    icon: 'el-icon-edit-outline',
  },
  {
    path: '/charts',
    name: '图表页',
    icon: 'el-icon-picture-outline',
  },
  {
    path: '/profile',
    name: '详情页',
    icon: 'el-icon-tickets',
    children: [
      {
        path: '/success',
        name: '基础详情页',
      },
      {
        path: '/fail',
        name: '失败',
      },
    ],
  },
];

export { headerMenuConfig, asideMenuConfig };
