import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import MainPage from '../mainPage';
import LoginPage from './login/login';
import LoginAsk from './login/loginAsk';
import Signup from './login/signup';

export default () => (
    <Router>
        <Routes>
            <Route exact path='/' element={<MainPage/>} />
            <Route path='/register' element={<Signup/>} />
            <Route path='/login' element={<LoginPage/>} />
            <Route path='/login-ask' element={<LoginAsk/>} />
        </Routes>
    </Router>
)