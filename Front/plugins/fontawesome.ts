import { library, config } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import {
  faGithub,
  faLinkedin,
  faTwitter,
  faFacebook,
  faGoogle,
} from "@fortawesome/free-brands-svg-icons";
import {
  faCalendarCheck,
  faCheck,
  faCheckCircle,
  faChevronDown,
  faChevronRight,
  faCircleNotch,
  faCircleUser,
  faEnvelope,
  faLocationDot,
  faLock,
  faPhone,
  faRightFromBracket,
  faShieldAlt,
  faStar,
  faTimesCircle,
  faExternalLinkAlt,
  faUser,
  faUserSlash,
  faTimes,
  faPenToSquare,
} from "@fortawesome/free-solid-svg-icons";

library.add(
  faGithub,
  faLinkedin,
  faTwitter,
  faFacebook,
  faGoogle,
  faCalendarCheck,
  faStar,
  faLocationDot,
  faCheckCircle,
  faTimesCircle,
  faCircleNotch,
  faShieldAlt,
  faPhone,
  faChevronRight,
  faChevronDown,
  faEnvelope,
  faLock,
  faCheck,
  faUser,
  faUserSlash,
  faCircleUser,
  faRightFromBracket,
  faExternalLinkAlt,
  faTimes,
  faPenToSquare
);

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.component("font-awesome-icon", FontAwesomeIcon);
});
