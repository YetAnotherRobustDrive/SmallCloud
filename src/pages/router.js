import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import MainPage from './mainPage';
import LoginPage from './component/login/login';
import LoginAsk from './component/login/loginAsk';
import Signup from './component/login/signup';
import Mypage from './component/mypage/mypage';
import AdminRules from './component/admin/rules';

export default () => (
    <Router>
        <Routes>
            <Route exact path='/' element={<MainPage/>} />
            <Route path='/register' element={<Signup/>} />
            <Route path='/login' element={<LoginPage/>} />
            <Route path='/login-ask' element={<LoginAsk/>} />
            <Route path='/mypage' element={<Mypage/>} />
            <Route path='/admin' element={<AdminRules/>} />
            <Route path='/admin/rules' element={<AdminRules/>} />
        </Routes>
    </Router>
)