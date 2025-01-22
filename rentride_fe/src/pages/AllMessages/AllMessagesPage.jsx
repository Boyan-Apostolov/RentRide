import React, { useEffect, useState } from "react";
import { answerMessage, getAllMessages } from "../../api/messagesService";
import NavBar from "../../components/NavBar/NavBar";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import { showInputPopup } from "../../api/utils/swalHelpers";
import Swal from "sweetalert2";

export default function AllMessagesPage() {
  const [messages, setMessages] = useState([]);
  const [isLoadingMessages, setLoadingMessages] = useState(true);

  useEffect(() => {
    fetchMessagesData();
  }, []);

  async function handleMessageReplyStart(messageId) {
    const inputValue = await showInputPopup("Message Reply: ");
    if (inputValue === null) return;

    await answerMessage(messageId, { answerContent: inputValue });
    Swal.fire("Success", "Reply has been sent!", "success");

    fetchMessagesData();
  }

  async function fetchMessagesData() {
    setLoadingMessages(true);
    const messagesData = await getAllMessages();

    setLoadingMessages(false);
    setMessages(messagesData);
  }

  return (
    <>
      <NavBar bg="light" />
      {isLoadingMessages ? (
        <LoadingSpinner />
      ) : (
        <div className=" overflow-scroll w-90 round-edge m-3 p-3 light-gray-background text-white m-auto ">
          <h2 className="text-center">All Messages</h2>
          <table className="table table-striped table-light round-edge">
            <thead>
              <tr>
                <th scope="col">#</th>
                <th scope="col">User</th>
                <th scope="col">Question</th>
                <th scope="col">Answer</th>
                <th scope="col">Action</th>
              </tr>
            </thead>
            <tbody>
              {messages.map((message) => {
                return (
                  <tr key={message.id}>
                    <th scope="row">{message.id}</th>
                    <td>{message.user.name}</td>
                    <td>{message.messageContent}</td>
                    <td>{message.answerContent || "-"}</td>
                    <td>
                      <button
                        className={`btn btn-primary ${
                          message.answerContent ? "disabled" : ""
                        } `}
                        onClick={() => handleMessageReplyStart(message.id)}
                      >
                        <i className="fa-solid fa-reply" title="Reply"></i>{" "}
                        Reply
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}
    </>
  );
}
