// В production переменная окружения может отсутствовать, поэтому используем относительный путь.
export const API_URL = process.env.REACT_APP_API_URL || '/api'; 