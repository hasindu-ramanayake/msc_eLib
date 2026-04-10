import React from 'react';
import { CalendarIcon, ClockIcon, MapPinIcon } from '@heroicons/react/24/outline';

const UpcomingEvents = () => {
  // Mock data for upcoming events
  const events = [
    {
      id: 1,
      title: 'Author Meet & Greet: Jane Doe',
      date: 'Oct 24, 2026',
      time: '6:00 PM - 8:00 PM',
      location: 'Main Hall',
      description: 'Join us for an evening with bestselling author Jane Doe as she discusses her latest sci-fi novel.',
      category: 'Author Event',
      color: 'bg-purple-100 text-purple-700 border-purple-200'
    },
    {
      id: 2,
      title: 'Web Development Workshop for Beginners',
      date: 'Oct 26, 2026',
      time: '1:00 PM - 4:00 PM',
      location: 'Tech Lab 2',
      description: 'Learn the basics of HTML, CSS, and JavaScript in this hands-on workshop. Bring your own laptop!',
      category: 'Workshop',
      color: 'bg-blue-100 text-blue-700 border-blue-200'
    },
    {
      id: 3,
      title: 'Community Book Club: The Great Gatsby',
      date: 'Oct 28, 2026',
      time: '7:00 PM - 8:30 PM',
      location: 'Reading Room B',
      description: 'Monthly book club discussion. We will be diving deep into the themes of the American Dream.',
      category: 'Book Club',
      color: 'bg-green-100 text-green-700 border-green-200'
    }
  ];

  return (
    <div className="w-full mt-12 mb-8 animate-fade-in-up" style={{ animationDelay: '0.1s' }}>
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-2xl font-bold text-gray-800 tracking-tight">Upcoming Events</h2>
        <a href="#" className="text-sm font-medium text-blue-600 hover:text-blue-800 transition-colors">
          View full calendar &rarr;
        </a>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {events.map((event) => (
          <div 
            key={event.id} 
            className="bg-white rounded-xl border border-gray-200 shadow-sm hover:shadow-md transition-shadow duration-300 overflow-hidden flex flex-col h-full group"
          >
            {/* Top accent line */}
            <div className={`h-1.5 w-full ${event.color.split(' ')[0]}`}></div>
            
            <div className="p-6 flex flex-col flex-grow">
              <div className="flex justify-between items-start mb-4">
                <span className={`text-xs font-semibold px-2.5 py-1 rounded-full border ${event.color}`}>
                  {event.category}
                </span>
              </div>
              
              <h3 className="text-lg font-bold text-gray-900 mb-2 group-hover:text-blue-600 transition-colors">
                {event.title}
              </h3>
              
              <p className="text-gray-600 text-sm mb-6 flex-grow line-clamp-3">
                {event.description}
              </p>
              
              <div className="space-y-2 mt-auto pt-4 border-t border-gray-100">
                <div className="flex items-center text-sm text-gray-500">
                  <CalendarIcon className="h-4 w-4 mr-2.5 text-gray-400" />
                  {event.date}
                </div>
                <div className="flex items-center text-sm text-gray-500">
                  <ClockIcon className="h-4 w-4 mr-2.5 text-gray-400" />
                  {event.time}
                </div>
                <div className="flex items-center text-sm text-gray-500">
                  <MapPinIcon className="h-4 w-4 mr-2.5 text-gray-400" />
                  {event.location}
                </div>
              </div>
            </div>
            
            <div className="px-6 py-3 bg-gray-50 border-t border-gray-100 mt-auto">
              <button className="w-full text-center text-sm font-medium text-blue-600 hover:text-blue-800 transition-colors">
                RSVP Now
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default UpcomingEvents;
