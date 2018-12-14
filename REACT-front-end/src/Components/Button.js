import React from 'react';

const Button = ({
    onClick,
    className = '',
    children,
  }) =>
    <button
      onClick={onClick}
      className={className}
      type="button"
    >
      {children}
    </button>

export default Button