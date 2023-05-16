import React from 'react';
import { useSelector } from 'react-redux';
import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom';
import LogoutUser from '../services/user/LogoutUser';
import AdminConfigPage from './admin/AdminConfigPage';
import AdminUserCtrlPage from './admin/AdminUserCtrlPage';
import AdminUserRegister from './admin/AdminUserRegister';
import ErrorPage from './common/ErrorPage';
import MainPage from './common/MainPage';
import FaqPage from './cs/FaqPage';
import NoticePage from './cs/NoticePage';
import QuestionPage from './cs/QuestionPage';
import TermsPage from './cs/TermsPage';
import DownloadPage from './load/DownloadPage';
import UploadPage from './load/UploadPage';
import LoginPage from './login/LoginPage';
import LoginQuestionPage from './login/LoginQuestionPage';
import RegisterPage from './login/RegisterPage';
import FavoritesPage from './main/FavoritesPage';
import TrashBinPage from './main/TrashBinPage';
import MyPage from './mypage/MyPage';
import PrivatePage from './mypage/PrivatePage';
import SecurityInfoPage from './mypage/SecurityInfoPage';
import ServiceInfoPage from './mypage/ServiceInfoPage';
import AdminPage from './admin/AdminPage';

export default () => {
    const isPrivileged = useSelector(state => state.token.isPrivileged);
    const isAdmin = useSelector(state => state.token.isAdmin);
    const isLoggedIn = useSelector(state => state.user.isLoggedIn);

    return (
        <Router>
            <Routes>
                <Route exact path='/' element={isLoggedIn ? <MainPage /> : <LoginPage />} />
                <Route path='/register' element={<RegisterPage />} />

                <Route path='/login' element={<LoginPage />} />
                <Route path='/login/ask' element={<LoginQuestionPage />} />
                {isLoggedIn &&
                    <>
                        <Route path='/logout' element={<LogoutUser />} />
                        <Route path='/favorites' element={<FavoritesPage />} />
                        <Route path='/trash' element={<TrashBinPage />} />

                        <Route path='/mypage' element={isPrivileged ? <PrivatePage /> : <MyPage />} />
                        <Route path='/mypage/service' element={isPrivileged ? <ServiceInfoPage /> : <MyPage />} />
                        <Route path='/mypage/security' element={isPrivileged ? <SecurityInfoPage /> : <MyPage />} />

                        <Route path='/upload' element={<UploadPage />} />
                        <Route path='/download' element={<DownloadPage />} />

                        <Route path='/cs/notice' element={<NoticePage />} />
                        <Route path='/cs/terms' element={<TermsPage />} />
                        <Route path='/cs/faq' element={<FaqPage />} />
                        <Route path='/cs/question' element={<QuestionPage />} />
                        {!isAdmin &&
                            <Route path='/admin' element={<AdminPage />} />
                        }
                        {isAdmin &&
                            <>
                                <Route path='/admin' element={<AdminConfigPage />} />
                                <Route path='/admin/rules' element={<AdminConfigPage />} />
                                <Route path='/admin/user' element={<AdminUserCtrlPage />} />
                                <Route path='/admin/user/register' element={<AdminUserRegister />} />
                            </>
                        }

                    </>
                }
                <Route path='/error' element={<ErrorPage />} />
                <Route path="*" element={<Navigate to="/error" />} />
            </Routes>
        </Router>
    )
}