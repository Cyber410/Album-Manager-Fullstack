// assets
import { PictureOutlined } from '@ant-design/icons';

// icons
const icons = {
  PictureOutlined
};

// ==============================|| MENU ITEMS - SAMPLE PAGE & DOCUMENTATION ||============================== //

const albums = {
  id: 'albums',
  title: 'Albums',
  type: 'group',
  children: [
    {
      id: 'Album',
      title: 'Albums',
      type: 'item',
      url: '/',
      icon: icons.PictureOutlined
    },
    {
      id: 'Add Album',
      title: 'Add Album',
      type: 'item',
      url: '/album/add',
      icon: icons.FileImageOutlined
    }
  ]
};

export default albums;
