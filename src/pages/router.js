import React from 'react';
import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom';
import MainPage from './mainPage';
import LoginPage from './login';
import LoginAsk from './loginAsk';
import Signup from './signup';
import Mypage from './mypage';
import AdminRules from './admin/rules';
import AdminUserCtrl from './admin/userCtrl';
import Upload from './upload';
import Download from './download';

export default () => (
    <Router>
        <Routes>
            <Route exact path='/' element={<MainPage/>} />
            <Route path='/register' element={<Signup/>} />
            <Route path='/login' element={<LoginPage/>} />
            <Route path='/login-ask' element={<LoginAsk/>} />
            <Route path='/mypage' element={<Mypage/>} />
            <Route path='/upload' element={<Upload/>} />
            <Route path='/download' element={<Download/>} />
            <Route path='/admin' element={<AdminRules/>} />
            <Route path='/admin/rules' element={<AdminRules/>} />
            <Route path='/admin/user-ctrl' element={<AdminUserCtrl/>} />
            <Route path="*" element={<Navigate to="/" />} />
        </Routes>
    </Router>
)