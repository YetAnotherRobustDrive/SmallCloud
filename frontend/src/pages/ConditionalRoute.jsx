import React from 'react';
import { useSelector } from 'react-redux';
import { Route, HashRouter as Router, Routes } from "react-router-dom";
import LogoutUser from '../services/user/LogoutUser';
import AdminConfigPage from './admin/AdminConfigPage';
import AdminFAQUploadPage from './admin/AdminFAQUploadPage';
import AdminGroupConfigPage from './admin/AdminGroupConfigPage';
import AdminLogPage from './admin/AdminLogPage';
import AdminNoticeUploadPage from './admin/AdminNoticeUploadPage';
import AdminPrivacyUploadPage from './admin/AdminPrivacyUploadPage';
import AdminQuestionList from './admin/AdminQuestionList';
import AdminTermUploadPage from './admin/AdminTermUploadPage';
import AdminUserCtrlPage from './admin/AdminUserCtrlPage';
import AdminUserRegister from './admin/AdminUserRegister';
import ErrorPage from './common/ErrorPage';
import FavoritesPage from './common/FavoritesPage';
import FolderPage from './common/FolderPage';
import MainPage from './common/MainPage';
import SearchPage from './common/SearchPage';
import SharePage from './common/SharePage';
import TrashBinPage from './common/TrashBinPage';
import FaqPage from './cs/FaqPage';
import Groups from './cs/Groups';
import NoticePage from './cs/NoticePage';
import QuestionPage from './cs/QuestionPage';
import TermsPage from './cs/TermsPage';
import LoginPage from './login/LoginPage';
import LoginQuestionPage from './login/LoginQuestionPage';
import RegisterPage from './login/RegisterPage';
import MyPage from './mypage/MyPage';
import PrivatePage from './mypage/PrivatePage';
import SecurityInfoPage from './mypage/SecurityInfoPage';
import ServiceInfoPage from './mypage/ServiceInfoPage';

export default function ConditionalRoute() {
    const isPrivileged = useSelector(state => state.token.isPrivileged);
    const isAdmin = useSelector(state => state.token.isAdmin);
    const isLoggedIn = useSelector(state => state.user.isLoggedIn);

    return (
        <Router>
            <Routes>
                    <>
                        {isAdmin &&
                            <>
                                <Route path='/login' element={<LoginPage />} />
                                <Route path='/logout' element={<LogoutUser />} />
                                <Route path='/' element={<AdminConfigPage />} />
                                <Route path='/admin' element={<AdminConfigPage />} />
                                <Route path='/admin/rules' element={<AdminConfigPage />} />
                                <Route path='/admin/user' element={<AdminUserCtrlPage />} />
                                <Route path='/admin/group' element={<AdminGroupConfigPage />} />
                                <Route path='/admin/user/register' element={<AdminUserRegister />} />
                                <Route path='/admin/questions' element={<AdminQuestionList />} />
                                <Route path='/admin/notice' element={<AdminNoticeUploadPage />} />
                                <Route path='/admin/faq' element={<AdminFAQUploadPage />} />
                                <Route path='/admin/term' element={<AdminTermUploadPage />} />
                                <Route path='/admin/privacy' element={<AdminPrivacyUploadPage />} />
                                <Route path='/admin/log' element={<AdminLogPage />} />
                            </>
                        }
                        {!isAdmin &&
                            <>
                                <Route exact path='/' element={isLoggedIn ? <MainPage /> : <LoginPage />} />
                                <Route path='/register' element={<RegisterPage />} />

                                <Route path='/login' element={<LoginPage />} />
                                <Route path='/login/ask' element={<LoginQuestionPage />} />
                                {isLoggedIn &&
                                    <>
                                        <Route path='/logout' element={<LogoutUser />} />
                                        <Route path='/favorites' element={<FavoritesPage />} />
                                        <Route path='/trash' element={<TrashBinPage />} />
                                        <Route path='/share' element={<SharePage />} />
                                        <Route path='/search' element={<SearchPage />} />

                                        <Route path='/folder/:fileID' element={<FolderPage />} />

                                        <Route path='/mypage' element={isPrivileged ? <PrivatePage /> : <MyPage />} />
                                        <Route path='/mypage/service' element={isPrivileged ? <ServiceInfoPage /> : <MyPage />} />
                                        <Route path='/mypage/security' element={isPrivileged ? <SecurityInfoPage /> : <MyPage />} />

                                        <Route path='/cs/notice' element={<NoticePage />} />
                                        <Route path='/cs/terms' element={<TermsPage />} />
                                        <Route path='/cs/faq' element={<FaqPage />} />
                                        <Route path='/cs/question' element={<QuestionPage />} />
                                        <Route path='/cs/groups' element={<Groups />} />

                                    </>
                                }
                            </>
                        }
                        <Route path="*" element={<ErrorPage />} />
                    </>
            </Routes>
        </Router>
    )
}