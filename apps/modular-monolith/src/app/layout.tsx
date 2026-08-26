import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Clinic Desk",
  description: "Prototype clinic modular monolith frontend",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}
