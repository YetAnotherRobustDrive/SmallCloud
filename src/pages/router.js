import React from 'react';
import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom';
import MainPage from './MainPage';
import LoginPage from './Login';
import LoginAsk from './LoginAsk';
import Signup from './Signup';
import Mypage from './Mypage';
import AdminRules from './admin/Rules';
import AdminUserCtrl from './admin/UserCtrl';
import Upload from './Upload';
import Download from './Download';
import Notice from './Notice';
import Terms from './Terms';
import FAQ from './FAQ';
import Ask from './Ask';
import ErrorPage from './ErrorPage';

export default () => (
    <Router>
        <Routes>
            <Route exact path='/' element={<MainPage/>} />
            <Route path='/register' element={<Signup/>} />
            <Route path='/login' element={<LoginPage/>} />
            <Route path='/login/ask' element={<LoginAsk/>} />
            
            <Route path='/mypage' element={<Mypage/>} />
            <Route path='/upload' element={<Upload/>} />
            <Route path='/download' element={<Download/>} />

            <Route path='/cs/notice' element={<Notice/>} />
            <Route path='/cs/terms' element={<Terms/>} />
            <Route path='/cs/faq' element={<FAQ/>} />
            <Route path='/cs/ask' element={<Ask/>} />

            <Route path='/admin' element={<AdminRules/>} />
            <Route path='/admin/rules' element={<AdminRules/>} />
            <Route path='/admin/user-ctrl' element={<AdminUserCtrl/>} />

            <Route path='/error' element={<ErrorPage/>} />
            <Route path="*" element={<Navigate to="/error" />} />
        </Routes>
    </Router>
)