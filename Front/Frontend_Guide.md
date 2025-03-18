# Guide du Frontend pour Presteo

Ce guide détaillé explique le fonctionnement du frontend de l'application Presteo, avec un focus particulier sur la création de pages et le fonctionnement des routeurs. Il est conçu pour permettre à n'importe qui de comprendre la structure et l'organisation du code, même sans connaissances préalables.

## Table des matières

1. [Introduction au Frontend](#introduction-au-frontend)
2. [Structure du projet Nuxt](#structure-du-projet-nuxt)
3. [Composants Vue.js](#composants-vuejs)
4. [Routage avec Nuxt.js](#routage-avec-nuxtjs)
5. [Gestion d'état avec Pinia](#gestion-détat-avec-pinia)
6. [Communication avec le backend](#communication-avec-le-backend)
7. [Styles avec Tailwind CSS](#styles-avec-tailwind-css)
8. [Bonnes pratiques](#bonnes-pratiques)
9. [Tests et déploiement](#tests-et-déploiement)

## Introduction au Frontend

Le frontend de Presteo est développé avec Nuxt.js, un framework basé sur Vue.js qui facilite la création d'applications web modernes. Nuxt offre des fonctionnalités avancées comme le rendu côté serveur (SSR), la génération de sites statiques, et un système de routage automatique, qui simplifient le développement.

### Technologies principales

- **Nuxt.js** : Framework pour applications Vue.js
- **Vue.js** : Framework JavaScript progressif pour construire des interfaces utilisateur
- **Tailwind CSS** : Framework CSS utilitaire pour styliser rapidement les composants
- **Pinia** : Bibliothèque de gestion d'état pour Vue.js
- **TypeScript** : Surcouche à JavaScript qui ajoute le typage statique

### Avantages de cette stack

- **Performance** : Optimisation du chargement des pages grâce au rendu côté serveur ou à la génération statique
- **Développement rapide** : Structure de projet prédéfinie et convention sur configuration
- **Maintenance facile** : Séparation claire des responsabilités et typage fort avec TypeScript
- **Expérience utilisateur fluide** : Navigation entre les pages sans rechargement complet

## Structure du projet Nuxt

Nuxt.js fournit une structure de projet organisée qui facilite le développement et la maintenance du code.

### Architecture des dossiers

```
Front/
├── assets/               # Fichiers à traiter (CSS, images, etc.)
│   └── css/
│       └── tailwind.css
├── components/           # Composants Vue réutilisables
│   └── Header.vue
├── pages/                # Pages de l'application (génère automatiquement les routes)
│   ├── index.vue
│   └── TestPage.vue
├── public/               # Fichiers statiques servis directement
│   ├── favicon.ico
│   └── robots.txt
├── server/               # Code côté serveur (API)
│   └── tsconfig.json
├── src/                  # Code source principal
│   └── app.vue           # Composant racine de l'application
├── package.json          # Dépendances et scripts
├── nuxt.config.ts        # Configuration Nuxt
└── tailwind.config.js    # Configuration Tailwind CSS
```

### Fichiers clés

#### src/app.vue

Le fichier `app.vue` est le composant racine de l'application. Dans Presteo, il contient la structure de base de toutes les pages.

```vue
<template>
  <Header />
  <NuxtPage />
</template>
```

Ce fichier simple inclut deux éléments :

- `<Header />` : Un composant de navigation présent sur toutes les pages
- `<NuxtPage />` : Un composant spécial qui affiche la page correspondant à la route actuelle

#### nuxt.config.ts

Le fichier de configuration central pour Nuxt.js qui définit les modules utilisés, les options de construction et autres paramètres.

```typescript
// Exemple simplifié de nuxt.config.ts
export default defineNuxtConfig({
  devtools: { enabled: true },
  modules: ["@nuxtjs/tailwindcss", "@pinia/nuxt"],
  css: ["~/assets/css/tailwind.css"],
  app: {
    head: {
      title: "Presteo",
      meta: [
        { charset: "utf-8" },
        { name: "viewport", content: "width=device-width, initial-scale=1" },
      ],
    },
  },
});
```

## Composants Vue.js

Les composants sont les blocs de construction d'une application Vue.js. Ils encapsulent le HTML, le CSS et le JavaScript nécessaires pour représenter une partie de l'interface utilisateur.

### Anatomie d'un composant Vue

Un composant Vue se compose généralement de trois sections :

1. **Template** : Structure HTML du composant
2. **Script** : Logique JavaScript/TypeScript du composant
3. **Style** : Styles CSS associés au composant

### Exemple de composant : Header.vue

```vue
<template>
  <header class="bg-blue-600 text-white p-4">
    <div class="container mx-auto flex justify-between items-center">
      <h1 class="font-bold text-xl">Presteo</h1>
      <nav>
        <ul class="flex space-x-4">
          <li><NuxtLink to="/" class="hover:underline">Accueil</NuxtLink></li>
          <li>
            <NuxtLink to="/test-page" class="hover:underline">Test</NuxtLink>
          </li>
          <!-- Autres liens de navigation -->
        </ul>
      </nav>
    </div>
  </header>
</template>

<script setup lang="ts">
// Logique du composant utilisant la Composition API
const isLoggedIn = ref(false);

// Méthodes
const toggleLogin = () => {
  isLoggedIn.value = !isLoggedIn.value;
};
</script>

<style scoped>
/* Styles spécifiques au composant */
header {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
```

### Options API vs Composition API

Vue.js propose deux façons d'écrire des composants :

**Options API** (Approche traditionnelle) :

```vue
<script>
export default {
  data() {
    return {
      message: "Bonjour",
    };
  },
  methods: {
    greet() {
      alert(this.message);
    },
  },
};
</script>
```

**Composition API** (Approche moderne avec `<script setup>`) :

```vue
<script setup>
import { ref } from "vue";

const message = ref("Bonjour");
const greet = () => {
  alert(message.value);
};
</script>
```

Dans Presteo, nous privilégions la Composition API avec `<script setup>` pour sa concision et sa meilleure prise en charge de TypeScript.

## Routage avec Nuxt.js

L'une des fonctionnalités les plus puissantes de Nuxt est son système de routage basé sur la structure des fichiers. Les routes sont automatiquement générées en fonction de l'arborescence des fichiers dans le dossier `pages/`.

### Routage basé sur la structure des fichiers

- `pages/index.vue` → `/`
- `pages/about.vue` → `/about`
- `pages/users/index.vue` → `/users`
- `pages/users/[id].vue` → `/users/:id` (route dynamique)
- `pages/users/profile.vue` → `/users/profile`

### Exemple de structure de routage

```
pages/
├── index.vue                # Route: /
├── login.vue                # Route: /login
├── register.vue             # Route: /register
├── dashboard.vue            # Route: /dashboard
├── users/
│   ├── index.vue            # Route: /users
│   ├── [id].vue             # Route: /users/:id
│   └── profile.vue          # Route: /users/profile
└── projects/
    ├── index.vue            # Route: /projects
    └── [projectId]/
        ├── index.vue        # Route: /projects/:projectId
        └── edit.vue         # Route: /projects/:projectId/edit
```

### Navigation entre les pages

Pour naviguer entre les pages, vous pouvez utiliser le composant `<NuxtLink>` (similaire à `<router-link>` dans Vue Router) :

```vue
<template>
  <div>
    <h1>Page d'accueil</h1>
    <NuxtLink to="/users">Voir tous les utilisateurs</NuxtLink>
    <NuxtLink :to="'/users/' + userId">Voir le profil</NuxtLink>
  </div>
</template>
```

Pour une navigation programmatique, utilisez la fonction `navigateTo` :

```vue
<script setup>
import { navigateTo } from "#app";

const goToUserProfile = (userId) => {
  navigateTo(`/users/${userId}`);
};
</script>
```

### Pages dynamiques avec paramètres

Les pages dynamiques sont créées en utilisant des crochets `[]` dans le nom du fichier. Par exemple, `pages/users/[id].vue` crée une route pour `/users/:id`.

```vue
<template>
  <div>
    <h1>Profil utilisateur</h1>
    <p>ID de l'utilisateur : {{ $route.params.id }}</p>
    <!-- ou avec composables -->
    <p>ID de l'utilisateur : {{ route.params.id }}</p>
  </div>
</template>

<script setup>
import { useRoute } from "vue-router";

const route = useRoute();
// Vous pouvez maintenant accéder à route.params.id
</script>
```

### Middleware de route

Les middlewares vous permettent d'exécuter du code avant de naviguer vers une page. Ils sont utiles pour l'authentification, la vérification des autorisations, etc.

Exemple de middleware d'authentification :

```javascript
// middleware/auth.ts
export default defineNuxtRouteMiddleware((to, from) => {
  // Simuler la vérification d'authentification
  const isAuthenticated = localStorage.getItem("user-token");

  // Rediriger vers la page de connexion si l'utilisateur n'est pas authentifié
  if (!isAuthenticated && to.path !== "/login") {
    return navigateTo("/login");
  }
});
```

Pour appliquer ce middleware à une page :

```vue
<script setup>
definePageMeta({
  middleware: "auth",
});
</script>
```

## Gestion d'état avec Pinia

Pinia est la solution recommandée pour la gestion d'état dans les applications Vue.js modernes. Elle remplace Vuex et offre une meilleure intégration avec TypeScript.

### Création d'un store

```typescript
// stores/users.ts
import { defineStore } from "pinia";

interface User {
  id: number;
  username: string;
  email: string;
}

export const useUsersStore = defineStore("users", {
  state: () => ({
    users: [] as User[],
    loading: false,
    error: null as string | null,
  }),

  getters: {
    getUserById: (state) => (id: number) => {
      return state.users.find((user) => user.id === id);
    },
  },

  actions: {
    async fetchUsers() {
      this.loading = true;
      this.error = null;

      try {
        const response = await fetch("/api/users");
        const data = await response.json();
        this.users = data;
      } catch (err: any) {
        this.error = err.message || "Une erreur est survenue";
      } finally {
        this.loading = false;
      }
    },

    async addUser(user: Omit<User, "id">) {
      // Logique pour ajouter un utilisateur
    },
  },
});
```

### Utilisation du store dans un composant

```vue
<template>
  <div>
    <h1>Liste des utilisateurs</h1>
    <div v-if="usersStore.loading">Chargement...</div>
    <div v-else-if="usersStore.error">Erreur: {{ usersStore.error }}</div>
    <ul v-else>
      <li v-for="user in usersStore.users" :key="user.id">
        {{ user.username }} ({{ user.email }})
      </li>
    </ul>
    <button @click="usersStore.fetchUsers()">Rafraîchir</button>
  </div>
</template>

<script setup>
import { useUsersStore } from "~/stores/users";
import { onMounted } from "vue";

const usersStore = useUsersStore();

onMounted(() => {
  usersStore.fetchUsers();
});
</script>
```

## Communication avec le backend

La communication avec le backend est essentiellement assurée par des requêtes HTTP. Nuxt offre plusieurs façons de gérer ces communications.

### Utilisation de Fetch API

```vue
<script setup>
import { ref } from "vue";

const users = ref([]);
const loading = ref(false);
const error = ref(null);

const fetchUsers = async () => {
  loading.value = true;
  try {
    const response = await fetch("http://localhost:8080/api/users");
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    users.value = await response.json();
  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
};

fetchUsers();
</script>
```

### Utilisation de composables personnalisés

```typescript
// composables/useApi.ts
import { ref } from "vue";

export function useApi<T>(endpoint: string) {
  const data = ref<T | null>(null);
  const loading = ref(false);
  const error = ref<string | null>(null);

  const baseUrl = "http://localhost:8080/api";

  const fetchData = async () => {
    loading.value = true;
    error.value = null;

    try {
      const response = await fetch(`${baseUrl}/${endpoint}`);
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      data.value = await response.json();
    } catch (err: any) {
      error.value = err.message || "Une erreur est survenue";
      data.value = null;
    } finally {
      loading.value = false;
    }
  };

  const postData = async (payload: any) => {
    loading.value = true;
    error.value = null;

    try {
      const response = await fetch(`${baseUrl}/${endpoint}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      return await response.json();
    } catch (err: any) {
      error.value = err.message || "Une erreur est survenue";
      throw err;
    } finally {
      loading.value = false;
    }
  };

  return {
    data,
    loading,
    error,
    fetchData,
    postData,
  };
}
```

Utilisation du composable dans un composant :

```vue
<template>
  <div>
    <div v-if="loading">Chargement...</div>
    <div v-else-if="error">Erreur: {{ error }}</div>
    <ul v-else>
      <li v-for="user in data" :key="user.id">{{ user.username }}</li>
    </ul>
    <button @click="fetchData">Rafraîchir</button>
  </div>
</template>

<script setup>
import { useApi } from "~/composables/useApi";

const { data, loading, error, fetchData } = useApi("users");

// Chargement initial
fetchData();
</script>
```

### Gestion des réponses et erreurs

Il est essentiel de bien gérer les réponses et les erreurs des requêtes API :

```typescript
const handleApiRequest = async (apiCall: () => Promise<any>) => {
  try {
    const result = await apiCall();
    // Traiter le succès
    return result;
  } catch (error) {
    if (error instanceof Response) {
      // Erreur HTTP avec un statut
      if (error.status === 401) {
        // Non autorisé - rediriger vers la page de connexion
        navigateTo("/login");
      } else if (error.status === 403) {
        // Accès refusé
        alert("Vous n'avez pas les droits nécessaires pour cette action");
      } else {
        // Autres erreurs HTTP
        alert(`Erreur serveur: ${error.status}`);
      }
    } else {
      // Erreurs réseau ou autres
      console.error("Erreur API:", error);
      alert("Une erreur est survenue lors de la communication avec le serveur");
    }
  }
};
```

## Styles avec Tailwind CSS

Tailwind CSS est un framework CSS utilitaire qui permet de construire rapidement des interfaces personnalisées sans écrire de CSS personnalisé.

### Configuration de base

Le fichier `tailwind.config.js` contient la configuration de Tailwind :

```javascript
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./components/**/*.{js,vue,ts}",
    "./layouts/**/*.vue",
    "./pages/**/*.vue",
    "./plugins/**/*.{js,ts}",
    "./app.vue",
  ],
  theme: {
    extend: {
      colors: {
        primary: "#3B82F6",
        secondary: "#10B981",
        // Autres couleurs personnalisées
      },
      fontFamily: {
        sans: ["Inter", "sans-serif"],
        // Autres familles de polices
      },
    },
  },
  plugins: [],
};
```

### Utilisation des classes Tailwind

Tailwind CSS utilise des classes utilitaires directement dans le HTML pour appliquer des styles :

```vue
<template>
  <div
    class="max-w-md mx-auto bg-white rounded-xl shadow-md overflow-hidden md:max-w-2xl"
  >
    <div class="md:flex">
      <div class="md:shrink-0">
        <img
          class="h-48 w-full object-cover md:h-full md:w-48"
          src="/img/building.jpg"
          alt="Modern building architecture"
        />
      </div>
      <div class="p-8">
        <div
          class="uppercase tracking-wide text-sm text-indigo-500 font-semibold"
        >
          Company retreats
        </div>
        <a
          href="#"
          class="block mt-1 text-lg leading-tight font-medium text-black hover:underline"
          >Incredible accommodation for your team</a
        >
        <p class="mt-2 text-slate-500">
          Looking to take your team away on a retreat to enjoy awesome food and
          take in some sunshine?
        </p>
      </div>
    </div>
  </div>
</template>
```

### Exemples de composants stylisés

#### Bouton

```vue
<template>
  <button
    class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
    @click="onClick"
  >
    {{ text }}
  </button>
</template>

<script setup>
defineProps({
  text: {
    type: String,
    default: "Cliquez-moi",
  },
});

const emit = defineEmits(["click"]);
const onClick = () => {
  emit("click");
};
</script>
```

#### Formulaire

```vue
<template>
  <form
    @submit.prevent="handleSubmit"
    class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4"
  >
    <div class="mb-4">
      <label class="block text-gray-700 text-sm font-bold mb-2" for="username">
        Nom d'utilisateur
      </label>
      <input
        class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        id="username"
        type="text"
        placeholder="Nom d'utilisateur"
        v-model="username"
      />
      <p v-if="errors.username" class="text-red-500 text-xs italic">
        {{ errors.username }}
      </p>
    </div>

    <div class="mb-6">
      <label class="block text-gray-700 text-sm font-bold mb-2" for="password">
        Mot de passe
      </label>
      <input
        class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        id="password"
        type="password"
        placeholder="******************"
        v-model="password"
      />
      <p v-if="errors.password" class="text-red-500 text-xs italic">
        {{ errors.password }}
      </p>
    </div>

    <div class="flex items-center justify-between">
      <button
        class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
        type="submit"
      >
        Se connecter
      </button>
      <a
        class="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800"
        href="#"
      >
        Mot de passe oublié?
      </a>
    </div>
  </form>
</template>

<script setup>
import { ref, reactive } from "vue";

const username = ref("");
const password = ref("");
const errors = reactive({
  username: "",
  password: "",
});

const handleSubmit = () => {
  // Réinitialiser les erreurs
  errors.username = "";
  errors.password = "";

  // Validation
  let isValid = true;

  if (!username.value) {
    errors.username = "Le nom d'utilisateur est requis";
    isValid = false;
  }

  if (!password.value) {
    errors.password = "Le mot de passe est requis";
    isValid = false;
  } else if (password.value.length < 8) {
    errors.password = "Le mot de passe doit comporter au moins 8 caractères";
    isValid = false;
  }

  if (isValid) {
    // Appeler l'API ou effectuer l'action désirée
    console.log("Formulaire soumis", {
      username: username.value,
      password: password.value,
    });
  }
};
</script>
```

## Bonnes pratiques

### Organisation du code

- **Composables** : Extraire la logique réutilisable dans des composables pour faciliter la maintenance
- **Typage** : Utiliser TypeScript pour réduire les erreurs et améliorer l'auto-complétion
- **Composants atomiques** : Créer de petits composants réutilisables (boutons, champs de formulaire, etc.)

### Performance

- **Chargement paresseux** : Utiliser `defineAsyncComponent` pour charger les composants uniquement quand nécessaire
- **Images optimisées** : Utiliser le composant `<nuxt-img>` pour optimiser automatiquement les images
- **Cache** : Mettre en cache les résultats d'API pour réduire les requêtes

### Sécurité

- **Sanitization** : Nettoyer les entrées utilisateur pour prévenir les attaques XSS
- **CSRF** : Utiliser des tokens pour protéger contre les attaques CSRF
- **Stockage sécurisé** : Stocker les informations sensibles dans des cookies sécurisés plutôt que localStorage

## Tests et déploiement

### Tests unitaires avec Vitest

```typescript
// tests/components/Button.test.ts
import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";
import Button from "~/components/Button.vue";

describe("Button Component", () => {
  it("renders properly", () => {
    const wrapper = mount(Button, {
      props: {
        text: "Tester",
      },
    });

    expect(wrapper.text()).toContain("Tester");
  });

  it("emits click event when clicked", async () => {
    const wrapper = mount(Button);
    await wrapper.trigger("click");

    expect(wrapper.emitted().click).toBeTruthy();
    expect(wrapper.emitted().click.length).toBe(1);
  });
});
```

### Tests de bout en bout avec Cypress

```javascript
// cypress/e2e/login.cy.js
describe("Login Page", () => {
  it("should login with valid credentials", () => {
    cy.visit("/login");

    cy.get("#username").type("testuser");
    cy.get("#password").type("password123");
    cy.get('button[type="submit"]').click();

    // Vérifier la redirection
    cy.url().should("include", "/dashboard");

    // Vérifier que l'utilisateur est connecté
    cy.get(".user-greeting").should("contain", "Bienvenue, testuser");
  });

  it("should show error with invalid credentials", () => {
    cy.visit("/login");

    cy.get("#username").type("testuser");
    cy.get("#password").type("wrongpassword");
    cy.get('button[type="submit"]').click();

    // Vérifier le message d'erreur
    cy.get(".alert-error").should("be.visible");
    cy.get(".alert-error").should("contain", "Identifiants incorrects");

    // Vérifier qu'on reste sur la page de connexion
    cy.url().should("include", "/login");
  });
});
```

### Déploiement

#### Configuration pour la production

```typescript
// nuxt.config.ts pour la production
export default defineNuxtConfig({
  // ...autres configurations

  runtimeConfig: {
    // Variables d'environnement côté serveur uniquement
    apiSecret: process.env.API_SECRET,

    // Variables d'environnement publiques (accessibles côté client)
    public: {
      apiBase: process.env.API_BASE || "https://api.presteo.com",
    },
  },

  app: {
    head: {
      title: "Presteo",
      meta: [
        { charset: "utf-8" },
        { name: "viewport", content: "width=device-width, initial-scale=1" },
        {
          name: "description",
          content: "Presteo - Plateforme de gestion de services",
        },
      ],
    },
  },
});
```

#### Script de déploiement

```json
// package.json
{
  "scripts": {
    "dev": "nuxt dev",
    "build": "nuxt build",
    "generate": "nuxt generate",
    "preview": "nuxt preview",
    "deploy:staging": "nuxt build && rsync -avz .output/ user@staging-server:/path/to/app/",
    "deploy:prod": "nuxt build && rsync -avz .output/ user@production-server:/path/to/app/"
  }
}
```

## Conclusion

Ce guide couvre les aspects fondamentaux du développement frontend avec Nuxt.js, Vue.js et les technologies associées. En comprenant ces principes et en suivant les bonnes pratiques, vous pouvez efficacement contribuer au développement et à la maintenance du frontend de l'application Presteo.

Les points clés à retenir :

- Le routage basé sur la structure des fichiers simplifie la navigation
- Les composants Vue.js permettent de créer des interfaces modulaires et réutilisables
- Pinia facilite la gestion de l'état global de l'application
- La communication avec le backend doit être bien structurée avec une gestion appropriée des erreurs
- Tailwind CSS permet de styliser rapidement les composants sans CSS personnalisé
- Les tests sont essentiels pour garantir la fiabilité de l'application

Avec ces connaissances, vous devriez être en mesure de comprendre, modifier et étendre le frontend de Presteo efficacement.
