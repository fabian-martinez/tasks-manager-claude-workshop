-- Task Manager API - Sample Data
-- This file is automatically loaded when the application starts
-- It provides initial data for testing and workshop demonstrations

-- Insert sample users
INSERT INTO users (id, name, email, created_at, updated_at) VALUES
(1, 'Alice Johnson', 'alice.johnson@taskmanager.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Bob Smith', 'bob.smith@taskmanager.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Charlie Davis', 'charlie.davis@taskmanager.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample projects
INSERT INTO projects (id, name, description, created_at, updated_at) VALUES
(1, 'Website Redesign', 'Complete redesign of the company website with modern UI/UX principles', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Mobile App Development', 'Development of cross-platform mobile application for iOS and Android', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample tasks with various statuses
INSERT INTO tasks (id, title, description, status, due_date, created_at, updated_at, completed_at, project_id, assigned_to_id, created_by_id) VALUES
-- Website Redesign Project Tasks
(1, 'Design Homepage Mockup', 'Create wireframes and mockups for the new homepage design including hero section, navigation, and footer', 'DONE', NULL, DATEADD('DAY', -15, CURRENT_TIMESTAMP), DATEADD('DAY', -10, CURRENT_TIMESTAMP), DATEADD('DAY', -10, CURRENT_TIMESTAMP), 1, 1, 1),
(2, 'Implement Responsive Navigation', 'Develop responsive navigation menu that works on desktop, tablet, and mobile devices', 'IN_PROGRESS', DATEADD('DAY', 7, CURRENT_TIMESTAMP), DATEADD('DAY', -8, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, NULL, 1, 2, 1),
(3, 'Setup Content Management System', 'Install and configure WordPress CMS with custom themes and plugins', 'TODO', DATEADD('DAY', 14, CURRENT_TIMESTAMP), DATEADD('DAY', -5, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, NULL, 1, 3, 2),
(4, 'Optimize Site Performance', 'Implement caching, image optimization, and CDN integration for better loading times', 'TODO', DATEADD('DAY', 21, CURRENT_TIMESTAMP), DATEADD('DAY', -3, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, NULL, 1, NULL, 2),

-- Mobile App Development Project Tasks
(5, 'Setup React Native Environment', 'Initialize React Native project with proper folder structure and dependencies', 'DONE', NULL, DATEADD('DAY', -12, CURRENT_TIMESTAMP), DATEADD('DAY', -8, CURRENT_TIMESTAMP), DATEADD('DAY', -8, CURRENT_TIMESTAMP), 2, 2, 3),
(6, 'Design App Login Flow', 'Create UX/UI designs for user authentication including login, register, and forgot password screens', 'IN_PROGRESS', DATEADD('DAY', 5, CURRENT_TIMESTAMP), DATEADD('DAY', -6, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, NULL, 2, 1, 3),
(7, 'Implement Push Notifications', 'Integrate Firebase Cloud Messaging for cross-platform push notifications', 'TODO', DATEADD('DAY', 10, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, NULL, 2, 3, 2),
(8, 'App Store Submission', 'Prepare app for submission to Apple App Store and Google Play Store', 'TODO', DATEADD('DAY', 30, CURRENT_TIMESTAMP), DATEADD('DAY', -1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, NULL, 2, NULL, 3);

-- Insert sample comments
INSERT INTO comments (id, content, created_at, updated_at, task_id, author_id) VALUES
(1, 'Great work on the homepage design! The new layout looks modern and user-friendly. I especially like the hero section with the call-to-action button.', DATEADD('DAY', -9, CURRENT_TIMESTAMP), DATEADD('DAY', -9, CURRENT_TIMESTAMP), 1, 2),
(2, 'I agree with Bob. The design is excellent. Should we also consider adding a testimonials section below the fold?', DATEADD('DAY', -9, CURRENT_TIMESTAMP), DATEADD('DAY', -9, CURRENT_TIMESTAMP), 1, 3),
(3, 'The navigation is working well on desktop, but I noticed some issues on mobile devices. The hamburger menu doesn''t close properly on iOS Safari.', DATEADD('DAY', -2, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP), 2, 1),
(4, 'Thanks for catching that! I''ll investigate the iOS Safari issue and implement a fix. Will test on multiple devices.', DATEADD('DAY', -1, CURRENT_TIMESTAMP), DATEADD('DAY', -1, CURRENT_TIMESTAMP), 2, 2),
(5, 'The React Native setup is complete. All dependencies are installed and the project structure follows best practices. Ready for development!', DATEADD('DAY', -7, CURRENT_TIMESTAMP), DATEADD('DAY', -7, CURRENT_TIMESTAMP), 5, 2),
(6, 'Working on the login flow designs. Should we include social login options (Google, Facebook) or stick to email/password for now?', DATEADD('DAY', -4, CURRENT_TIMESTAMP), DATEADD('DAY', -4, CURRENT_TIMESTAMP), 6, 1),
(7, 'Let''s start with email/password authentication and add social login in the next iteration. Keep it simple for the MVP.', DATEADD('DAY', -3, CURRENT_TIMESTAMP), DATEADD('DAY', -3, CURRENT_TIMESTAMP), 6, 3);

-- Reset sequence counters for H2 database
ALTER SEQUENCE USERS_SEQ RESTART WITH 4;
ALTER SEQUENCE PROJECTS_SEQ RESTART WITH 3;
ALTER SEQUENCE TASKS_SEQ RESTART WITH 9;
ALTER SEQUENCE COMMENTS_SEQ RESTART WITH 8;