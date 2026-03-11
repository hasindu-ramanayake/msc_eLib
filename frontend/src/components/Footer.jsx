import React from 'react';

// const Footer = () => {
//   return (
//     <footer className="w-full bg-white border-t border-gray-200 mt-auto">
//       <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
//         <div className="grid grid-cols-1 md:grid-cols-4 gap-8">

//           {/* Brand & About */}
//           <div className="col-span-1 md:col-span-1">
//             <span className="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-700 to-indigo-600 tracking-tight block mb-4">
//               E-Library
//             </span>
//             <p className="text-sm text-gray-500 mb-6">
//               Your gateway to knowledge. Discover millions of resources, from classic literature to modern scientific research, all in one place.
//             </p>
//           </div>

//           {/* Quick Links */}
//           <div>
//             <h3 className="text-sm font-semibold text-gray-900 tracking-wider uppercase mb-4">
//               Explore
//             </h3>
//             <ul className="space-y-3">
//               <li><a href="#" className="text-sm text-gray-500 hover:text-blue-600 transition-colors">Digital Catalog</a></li>
//               <li><a href="#" className="text-sm text-gray-500 hover:text-blue-600 transition-colors">Audiobooks</a></li>
//               <li><a href="#" className="text-sm text-gray-500 hover:text-blue-600 transition-colors">Academic Journals</a></li>
//               <li><a href="#" className="text-sm text-gray-500 hover:text-blue-600 transition-colors">New Releases</a></li>
//             </ul>
//           </div>

//           {/* Library Services */}
//           <div>
//             <h3 className="text-sm font-semibold text-gray-900 tracking-wider uppercase mb-4">
//               Services
//             </h3>
//             <ul className="space-y-3">
//               <li><a href="#" className="text-sm text-gray-500 hover:text-blue-600 transition-colors">My Account</a></li>
//               <li><a href="#" className="text-sm text-gray-500 hover:text-blue-600 transition-colors">Room Reservations</a></li>
//               <li><a href="#" className="text-sm text-gray-500 hover:text-blue-600 transition-colors">Suggest a Purchase</a></li>
//               <li><a href="#" className="text-sm text-gray-500 hover:text-blue-600 transition-colors">Accessibility</a></li>
//             </ul>
//           </div>

//           {/* Connect */}
//           <div>
//             <h3 className="text-sm font-semibold text-gray-900 tracking-wider uppercase mb-4">
//               Connect
//             </h3>
//             <ul className="space-y-3">
//               <li><a href="#" className="text-sm text-gray-500 hover:text-blue-600 transition-colors">Contact Us</a></li>
//               <li><a href="#" className="text-sm text-gray-500 hover:text-blue-600 transition-colors">Library Locations</a></li>
//               <li><a href="#" className="text-sm text-gray-500 hover:text-blue-600 transition-colors">Newsletter</a></li>
//               <li><a href="#" className="text-sm text-gray-500 hover:text-blue-600 transition-colors">Volunteering</a></li>
//             </ul>
//           </div>
//         </div>

//         {/* Bottom Bar */}
//         <div className="mt-12 pt-8 border-t border-gray-100 flex flex-col md:flex-row justify-between items-center space-y-4 md:space-y-0">
//           <p className="text-sm text-gray-400">
//             &copy; {new Date().getFullYear()} E-Library System. All rights reserved.
//           </p>
//           <div className="flex space-x-6">
//             <a href="#" className="text-sm text-gray-400 hover:text-gray-900">Privacy Policy</a>
//             <a href="#" className="text-sm text-gray-400 hover:text-gray-900">Terms of Service</a>
//           </div>
//         </div>
//       </div>
//     </footer>
//   );
// };

const Footer = () => {
  return (
    <footer className="w-full bg-white border-t border-gray-200 mt-auto py-6">
      <div className="max-w-7xl mx-auto px-4 flex justify-center items-center">
        <p className="text-gray-500 text-sm">
          [Dummy Footer Component] &copy; {new Date().getFullYear()} E-Library
        </p>
      </div>
    </footer>
  );
};

export default Footer;
