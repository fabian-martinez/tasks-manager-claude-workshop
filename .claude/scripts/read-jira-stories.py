#!/usr/bin/env python3
import sys
import json
import os
import glob

def main():
    try:
        input_data = json.load(sys.stdin)
    except json.JSONDecodeError:
        input_data = {}

    cwd = input_data.get('cwd', os.getcwd())

    stories_dir = os.path.join(cwd, 'src/main/resources/mock-jira-stories')
    if not os.path.exists(stories_dir):
        # Allow the command to succeed without adding context if the directory doesn't exist
        print(json.dumps({"continue": True}))
        return

    stories_content = []

    # Check if the prompt mentions any specific task like TASK-001
    prompt = input_data.get('prompt', '').upper()

    for filename in sorted(os.listdir(stories_dir)):
        if filename.endswith('.md'):
            filepath = os.path.join(stories_dir, filename)
            # Try to see if a specific task is mentioned in the prompt
            task_id = filename.split('-')[0] + '-' + filename.split('-')[1] # e.g. TASK-001

            # If prompt mentions specific task, only include that one, else include all short summaries
            if task_id in prompt or not prompt:
                with open(filepath, 'r', encoding='utf-8') as f:
                    content = f.read()
                    stories_content.append(f"--- {filename} ---\n{content}\n")
            elif "TASK" in prompt:
                # Optimization: if the prompt asks for a specific task but we are in this block,
                # it means we didn't match the filename exactly, or it mentions a different task.
                pass
            else:
                # Include them all if no specific task is requested
                with open(filepath, 'r', encoding='utf-8') as f:
                    content = f.read()
                    # Just reading the first few lines as a summary to avoid overflowing context
                    summary = "\n".join(content.split("\n")[:10])
                    stories_content.append(f"--- {filename} (Summary) ---\n{summary}\n...\n")


    if stories_content:
        context_string = "Available Jira Stories from src/main/resources/mock-jira-stories:\n\n" + "\n".join(stories_content)

        output = {
            "hookSpecificOutput": {
                "hookEventName": "UserPromptSubmit",
                "additionalContext": context_string
            }
        }
        print(json.dumps(output))
    else:
        print(json.dumps({"continue": True}))

if __name__ == '__main__':
    main()
