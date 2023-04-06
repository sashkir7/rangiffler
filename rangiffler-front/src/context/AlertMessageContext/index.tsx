import { createContext, ReactNode, useMemo, useState } from "react";

interface AlertMessageContextInterface {
    error: string | null;
    addError: (error: string) => void;
    message: string | null;
    addMessage: (message: string) => void;
}


const defaultState = {
    error: null,
    addError: () => {},
    message: null,
    addMessage: () => {},
};
export const AlertMessageContext = createContext<AlertMessageContextInterface>(defaultState);

interface AlertMessageProviderInterface {
    children: ReactNode;
}
export const AlertMessageProvider = ({children}: AlertMessageProviderInterface) => {
    const [error, setError] = useState<string | null>(null);
    const [message, setMessage] = useState<string | null>(null);
    const addError = (error: string) => {
        setError(error)
        setTimeout(() => {
            setError(null);
        }, 3000);
    };

    const addMessage = (message: string) => {
        setMessage(message);
        setTimeout(() => {
            setMessage(null);
        }, 3000);
    };


    const alertContextValue = useMemo(
        () => ({ error, addError, message, addMessage}),
        [error, message]
    );

    return (
      <AlertMessageContext.Provider value={alertContextValue}>
          {children}
      </AlertMessageContext.Provider>
    );
}
