import React from "react";

export type VarianteBotao = "primario" | "perigo" | "neutro";

export interface BotaoProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variante?: VarianteBotao;
  variant?: VarianteBotao;
  children: React.ReactNode;
}

const variantClasses: Record<VarianteBotao, string> = {
  primario:
    "bg-blue-600 hover:bg-blue-700 text-white font-medium px-4 py-2 rounded-lg transition-colors cursor-pointer disabled:opacity-50",
  perigo:
    "bg-red-600 hover:bg-red-700 text-white font-medium px-4 py-2 rounded-lg transition-colors cursor-pointer disabled:opacity-50",
  neutro:
    "bg-gray-200 hover:bg-gray-300 text-gray-800 font-medium px-4 py-2 rounded-lg transition-colors cursor-pointer disabled:opacity-50",
};

export const Botao: React.FC<BotaoProps> = ({
  variante,
  variant,
  className = "",
  children,
  type = "button",
  ...props
}) => {
  const chosenVariant: VarianteBotao = variante || variant || "primario";
  const classes = `${variantClasses[chosenVariant]} ${className}`.trim();

  return (
    <button type={type} className={classes} {...props}>
      {children}
    </button>
  );
};

export default Botao;
