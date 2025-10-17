# Issues with Git Workflow and Commit Consistency

In the early stages of the project, we didn’t fully understand the importance of commits and issues, which led to several problems. Our commit messages were inconsistent and often unrelated to the tasks we were working on. As we incorporated issues and commits into our workflow, we struggled to maintain consistency. Team members described tasks differently, and some used IntelliJ, which had a different conventional commit extension compared to VS Code. This created further confusion. Additionally, some commits weren’t linked to the correct user in GitLab. For example, Mari’s changes were mistakenly attributed to Christina after switching devices. We also didn’t initially include branch numbers in our commits, which made project tracking more difficult.


To improve our commmit consistency, we studied lecture material on the topic Git and conventional commits, and discussed best practices as a team. We installed conventional commit extensions in VS Code to standardize our commit messages. A template was created to ensure consistency across the team, linking commit messages to issues correctly. We also emphasized the importance of verifying Git configurations, particularly after switching devices, to prevent misattribution. These steps improved the structure and clarity of our commit history.


We learned that consistency is essential for maintaining an organized Git history. Adopting a standardized commit format makes projects easier to track and manage. While challenging to implement initially, conventional commits proved to be a valuable tool. Additionally, we gained a deeper understanding of Git’s stages, resolving merge conflicts, and managing branches effectively.

# Setting Up Modules and Understanding Their Interconnections

A significant challenge was setting up modular components and aligning them with the three-layer architecture (presentation, business logic, and data access). Although modularity seemed straightforward in theory, implementing it was more complex. We struggled to define module responsibilities and ensure seamless interactions across layers. For instance, we debated whether validation logic belonged in the booking module, the controller, or a separate validation module. Data handling was similarly unclear, leading to inconsistencies and redundant code. These issues slowed development and caused bugs when modules didn’t interact as expected.


We studied modularity principles and how they align with the three-layer architecture, revisiting lecture materials and examples. Responsibilities were clarified:

* The presentation layer gathered user input and displayed results.
* The business logic layer managed bookings and validation.
* The data access layer handled database interactions.
We created diagrams mapping data flow and dependencies to understand layer interactions. Iterative work allowed us to refactor modules, such as moving validation logic into its own reusable module. This improved maintainability and ensured changes in one module didn’t disrupt others.


We realized the importance of defining architecture and module responsibilities early. Modular components must have clear roles to ensure maintainability and scalability. By focusing on separation of concerns and iterative improvements, we improved our understanding of how modules and layers interact. This experience reinforced the need for careful planning and alignment in future projects.
