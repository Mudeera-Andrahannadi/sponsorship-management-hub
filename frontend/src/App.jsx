import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { Toaster } from 'sonner';
import { AuthProvider } from './auth.jsx';
import Layout from './components/Layout.jsx';
import Login from './pages/Login.jsx';
import Companies from './pages/Companies.jsx';
import Events from './pages/Events.jsx';

export default function App()
{
    return (
        <BrowserRouter>
            <AuthProvider>
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route element={<Layout />}>
                        <Route index element={<Companies />} />
                        <Route path="events" element={<Events />} />
                    </Route>
                    <Route path="*" element={<Navigate to="/" replace />} />
                </Routes>
                <Toaster position="bottom-right" />
            </AuthProvider>
        </BrowserRouter>
    );
}
