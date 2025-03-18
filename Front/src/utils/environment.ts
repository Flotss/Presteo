/**
 * Configuration d'environnement pour Presteo
 * Ce fichier centralise l'accès aux variables d'environnement
 */

// On utilise le runtime config de Nuxt pour accéder aux variables d'environnement
import { useRuntimeConfig } from "#app";

/**
 * Interface décrivant les configurations d'environnement
 */
export interface Environment {
  apiBaseUrl: string;
  appName: string;
  appVersion: string;
  appEnv: string;
  enableLogs: boolean;
}

/**
 * Fonction pour obtenir les configurations d'environnement
 * @returns Configuration d'environnement typée
 */
export const useEnvironment = (): Environment => {
  // Récupérer la configuration runtime de Nuxt
  const config = useRuntimeConfig();

  return {
    // URL de base pour les API
    apiBaseUrl: config.public.apiBaseUrl as string,

    // Informations sur l'application
    appName: config.public.appName as string,
    appVersion: config.public.appVersion as string,
    appEnv: config.public.appEnv as string,

    // Configuration des logs
    enableLogs: config.public.enableLogs === "true",
  };
};

/**
 * Vérifie si l'application est en environnement de développement
 */
export const isDevelopment = (): boolean => {
  return useEnvironment().appEnv === "development";
};

/**
 * Vérifie si l'application est en environnement de production
 */
export const isProduction = (): boolean => {
  return useEnvironment().appEnv === "production";
};

/**
 * Logger conditionnel qui n'affiche les logs qu'en développement ou si enableLogs est true
 */
export const envLogger = {
  log: (...args: any[]): void => {
    const env = useEnvironment();
    if (isDevelopment() || env.enableLogs) {
      console.log("[PRESTEO]", ...args);
    }
  },
  error: (...args: any[]): void => {
    const env = useEnvironment();
    if (isDevelopment() || env.enableLogs) {
      console.error("[PRESTEO ERROR]", ...args);
    }
  },
  warn: (...args: any[]): void => {
    const env = useEnvironment();
    if (isDevelopment() || env.enableLogs) {
      console.warn("[PRESTEO WARNING]", ...args);
    }
  },
};
