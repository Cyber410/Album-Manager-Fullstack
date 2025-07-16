import { lazy } from 'react';

// project import
import Loadable from 'components/Loadable';
import MainLayout from 'layout/MainLayout';

// render - sample page
const AlbumPage = Loadable(lazy(() => import('pages/albums/albums')));
const AboutPage = Loadable(lazy(() => import('pages/staticPages/about')));
const AddAlbumPage = Loadable(lazy(() => import('pages/albums/albumAdd')));
const ShowAlbumPage = Loadable(lazy(() => import('pages/albums/albumShow')));
const UploadAlbumPage = Loadable(lazy(() => import('pages/albums/albumUpload')));
const EditAlbumPage = Loadable(lazy(() => import('pages/albums/albumEdit')));

const EditPhotoPage = Loadable(lazy(() => import('pages/albums/photoEdit')));

// ==============================|| MAIN ROUTING ||============================== //

const MainRoutes = {
  path: '/',
  element: <MainLayout />,
  children: [
    {
      path: '/',
      element: <AlbumPage />
    },
    {
      path: '/album/add',
      element: <AddAlbumPage/>
    },
    {
      path: '/album/show',
      element: <ShowAlbumPage/>
    },
    {
      path: '/photo/edit',
      element: <EditPhotoPage/>
    },
    {
      path: '/album/upload',
      element: <UploadAlbumPage/>
    },
    {
      path: '/album/edit',
      element: <EditAlbumPage/>
    },
    {
      path: '/about',
      element: <AboutPage />
    }

  ]
};

export default MainRoutes;
