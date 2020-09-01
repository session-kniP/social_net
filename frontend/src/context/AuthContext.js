import React from 'react';

export const AuthContext = React.createContext({
    login: function() {}, 
    logout: function() {}
});