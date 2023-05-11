import React from 'react';
import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom';
import LogoutUser from '../services/user/LogoutUser';
import AdminConfigPage from './admin/AdminConfigPage';
import AdminUserCtrlPage from './admin/AdminUserCtrlPage';
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

export default () => (
    <Router>
        <Routes>
            <Route exact path='/' element={<MainPage />} />
            <Route path='/favorites' element={<FavoritesPage />} />
            <Route path='/trash' element={<TrashBinPage />} />


            <Route path='/register' element={<RegisterPage/>} />
            <Route path='/login' element={<LoginPage/>} />
            <Route path='/login/ask' element={<LoginQuestionPage/>} />
            <Route path='/logout' element={<LogoutUser/>} />
        
            <Route path='/mypage' element={<MyPage link={<PrivatePage/>}/>} />
            <Route path='/mypage/service' element={<MyPage link={<ServiceInfoPage />} />} />
            <Route path='/mypage/security' element={<MyPage link={<SecurityInfoPage />} />} />

            <Route path='/upload' element={<UploadPage/>} />
            <Route path='/download' element={<DownloadPage />} />

            <Route path='/cs/notice' element={<NoticePage/>} />
            <Route path='/cs/terms' element={<TermsPage/>} />
            <Route path='/cs/faq' element={<FaqPage/>} />
            <Route path='/cs/question' element={<QuestionPage/>} />

            <Route path='/admin' element={<AdminConfigPage />} />
            <Route path='/admin/rules' element={<AdminConfigPage/>} />
            <Route path='/admin/user-ctrl' element={<AdminUserCtrlPage/>} />

            <Route path='/error' element={<ErrorPage/>} />
            <Route path="*" element={<Navigate to="/error" />} />
        </Routes>
    </Router>
)